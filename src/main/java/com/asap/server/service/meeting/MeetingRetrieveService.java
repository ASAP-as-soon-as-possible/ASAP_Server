package com.asap.server.service.meeting;

import static com.asap.server.common.exception.Error.MEETING_VALIDATION_FAILED_EXCEPTION;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.service.meeting.dto.BestMeetingTimeDto;
import com.asap.server.service.meeting.dto.UserDto;
import com.asap.server.service.time.MeetingTimeRecommendService;
import com.asap.server.service.time.UserMeetingScheduleService;
import com.asap.server.service.time.dto.retrieve.AvailableDatesRetrieveDto;
import com.asap.server.service.time.dto.retrieve.TimeSlotRetrieveDto;
import com.asap.server.service.time.dto.retrieve.TimeTableRetrieveDto;
import com.asap.server.service.time.vo.BestMeetingTimeVo;
import com.asap.server.service.time.vo.BestMeetingTimeWithUsers;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.user.UserRetrieveService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingRetrieveService {
    private final MeetingRepository meetingRepository;
    private final UserRetrieveService userRetrieveService;
    private final MeetingTimeRecommendService meetingTimeRecommendService;
    private final UserMeetingScheduleService userMeetingScheduleService;

    public BestMeetingTimeDto getBestMeetingTime(final Long meetingId, final Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
        if (meeting.isConfirmedMeeting()) {
            throw new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION);
        }

        int userCount = userRetrieveService.getMeetingUserCount(meeting);

        List<TimeBlockVo> timeBlocks = userMeetingScheduleService.getTimeBlocks(meetingId);

        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(
                timeBlocks,
                meeting.getDuration(),
                userCount
        );

        Map<Long, User> userIdToUserMap = userRetrieveService.getUserIdToUserMap(meetingId);
        List<BestMeetingTimeWithUsers> bestMeetingTimeWithUsers = bestMeetingTimes.stream()
                .map(bestMeetingTime -> mapToBestMeetingTimeWithUsers(bestMeetingTime, userIdToUserMap))
                .toList();
        return BestMeetingTimeDto.of(userCount, bestMeetingTimeWithUsers);
    }

    private BestMeetingTimeWithUsers mapToBestMeetingTimeWithUsers(
            final BestMeetingTimeVo bestMeetingTime,
            final Map<Long, User> userIdToUserMap
    ) {
        if (bestMeetingTime == null) {
            return null;
        }

        List<UserDto> userDtos = bestMeetingTime.userIds().stream()
                .map(userId -> {
                    User user = userIdToUserMap.get(userId);
                    return new UserDto(user.getId(), user.getName());
                })
                .toList();

        return new BestMeetingTimeWithUsers(
                bestMeetingTime.date(),
                bestMeetingTime.startTime(),
                bestMeetingTime.endTime(),
                bestMeetingTime.weight(),
                userDtos
        );
    }

    @Transactional(readOnly = true)
    public TimeTableRetrieveDto getTimeTable(final Long meetingId, final Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
        if (meeting.isConfirmedMeeting()) {
            throw new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION);
        }

        List<String> userNames = userRetrieveService.getUsersFromMeetingId(meetingId).stream().map(User::getName).toList();

        return TimeTableRetrieveDto.of(userNames, getAvailableDatesDto(meetingId, userNames.size()));

    }

    private List<AvailableDatesRetrieveDto> getAvailableDatesDto(final Long meetingId, final int totalUserCount) {
        List<TimeBlockVo> timeBlockVos = userMeetingScheduleService.getTimeBlocks(meetingId);

        Map<LocalDate, List<TimeSlotRetrieveDto>> timeSlotDtoMappedByDate = getTimeTableMapFromTimeBlockVo(timeBlockVos, totalUserCount);

        return timeSlotDtoMappedByDate.keySet().stream().map(
                date -> AvailableDatesRetrieveDto.of(
                        date,
                        timeSlotDtoMappedByDate.get(date)
                )
        ).toList();
    }

    private Map<LocalDate, List<TimeSlotRetrieveDto>> getTimeTableMapFromTimeBlockVo(final List<TimeBlockVo> timeBlockVo, final int totalUserCount) {
        return timeBlockVo.stream()
                .collect(Collectors.groupingBy(
                        TimeBlockVo::availableDate,
                        Collectors.mapping(t -> new TimeSlotRetrieveDto(
                                        t.timeSlot().getTime(),
                                        userRetrieveService.getUserNamesFromId(t.userIds()),
                                        setColorLevel(totalUserCount, t.userIds().size())
                                ),
                                Collectors.toList()
                        )
                ));
    }

    public int setColorLevel(final int memberCount, final int availableUserCount) {
        double ratio = (double) availableUserCount / memberCount;

        if (ratio <= 0.2) {
            return 1;
        } else if (ratio <= 0.4) {
            return 2;
        } else if (ratio <= 0.6) {
            return 3;
        } else if (ratio <= 0.8) {
            return 4;
        } else if (ratio <= 1.0) {
            return 5;
        } else {
            return 0;
        }
    }
}
