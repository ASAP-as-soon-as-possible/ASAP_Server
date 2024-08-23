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
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.user.UserRetrieveService;
import com.asap.server.service.time.vo.BestMeetingTimeVo;
import com.asap.server.service.time.vo.BestMeetingTimeWithUsers;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
