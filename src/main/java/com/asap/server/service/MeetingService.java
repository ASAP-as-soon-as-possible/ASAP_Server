package com.asap.server.service;

import com.asap.server.common.utils.BestMeetingUtil;
import com.asap.server.common.utils.TimeTableUtil;
import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.IsFixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.Place;
import com.asap.server.domain.User;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.MeetingTimeRepository;
import com.asap.server.repository.MeetingV2Repository;
import com.asap.server.service.vo.MeetingTimeVo;
import com.asap.server.service.vo.MeetingVo;
import com.asap.server.service.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.asap.server.exception.Error.INVALID_MEETING_HOST_EXCEPTION;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingTimeRepository meetingTimeRepository;
    private final MeetingV2Repository meetingV2Repository;
    private final UserV2Service userV2Service;
    private final AvailableDateService availableDateService;
    private final PreferTimeService preferTimeService;
    private final JwtService jwtService;
    private final UserService userService;
    private final BestMeetingUtil bestMeetingUtil;
    private final TimeTableUtil timeTableUtil;


    @Transactional
    public MeetingSaveResponseDto create(final MeetingSaveRequestDto meetingSaveRequestDto) {

        MeetingV2 meeting = MeetingV2.builder()
                .title(meetingSaveRequestDto.getTitle())
                .password(meetingSaveRequestDto.getPassword())
                .additionalInfo(meetingSaveRequestDto.getAdditionalInfo())
                .duration(meetingSaveRequestDto.getDuration())
                .place(
                        Place.builder()
                                .placeType(meetingSaveRequestDto.getPlace())
                                .placeDetail(meetingSaveRequestDto.getPlaceDetail())
                                .build())
                .build();

        meetingV2Repository.save(meeting);

        UserV2 host = userV2Service.createHost(meeting, meetingSaveRequestDto.getName());

        preferTimeService.create(meeting, meetingSaveRequestDto.getPreferTimes());
        availableDateService.create(meeting, meetingSaveRequestDto.getAvailableDates());

        meeting.setHost(host);

        String accessToken = jwtService.issuedToken(host.getId().toString());
        meeting.setUrl(Base64Utils.encodeToUrlSafeString(meeting.getId().toString().getBytes()));

        return MeetingSaveResponseDto.builder()
                .url(meeting.getUrl())
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public void confirmMeeting(
            MeetingConfirmRequestDto meetingConfirmRequestDto,
            Long meetingId,
            Long userId
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!userId.equals(meeting.getHost().getId()))
            throw new BadRequestException(INVALID_MEETING_HOST_EXCEPTION);

        meeting.setMonth(meetingConfirmRequestDto.getMonth());
        meeting.setDay(meetingConfirmRequestDto.getDay());
        meeting.setDayOfWeek(meetingConfirmRequestDto.getDayOfWeek());
        meeting.setStartTime(meetingConfirmRequestDto.getStartTime());
        meeting.setEndTime(meetingConfirmRequestDto.getEndTime());
        meeting.setFinalUsers(userService.getFixedUsers(meetingConfirmRequestDto.getUsers()));
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponseDto getMeetingSchedule(Long meetingId) {
        MeetingV2 meeting = meetingV2Repository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        return MeetingScheduleResponseDto.builder()
                .duration(meeting.getDuration())
                .placeType(meeting.getPlace().getPlaceType())
                .placeDetail(meeting.getPlace().getPlaceDetail())
                .availableDates(availableDateService.getAvailableDates(meeting))
                .preferTimes(preferTimeService.getPreferTimes(meeting))
                .build();
    }

    public FixedMeetingResponseDto getFixedMeetingInformation(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        List<String> userNames = meeting
                .getFixedUsers()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());

        return FixedMeetingResponseDto
                .builder()
                .title(meeting.getTitle())
                .place(meeting.getPlaceType().toString())
                .placeDetail(meeting.getPlaceDetail())
                .month(Integer.valueOf(meeting.getMonth()).toString())
                .day(Integer.valueOf(meeting.getDay()).toString())
                .dayOfWeek(meeting.getDayOfWeek())
                .startTime(meeting.getStartTime().getTime())
                .endTime(meeting.getEndTime().getTime())
                .hostName(meeting.getHost().getName())
                .userNames(userNames)
                .additionalInfo(meeting.getAdditionalInfo())
                .build();
    }

    public TimeTableResponseDto getTimeTable(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        if (!meeting.getHost().getId().equals(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
        List<User> users = meeting.getUsers();
        timeTableUtil.init();
        for (User user : users) {
            UserVo userVo = UserVo.of(user);
            List<com.asap.server.service.vo.MeetingTimeVo> meetingTimes = meetingTimeRepository.findByUser(user)
                    .stream()
                    .map(com.asap.server.service.vo.MeetingTimeVo::of)
                    .collect(Collectors.toList());
            timeTableUtil.setTimeTable(userVo, meetingTimes);
        }
        timeTableUtil.setColorLevel();
        return TimeTableResponseDto
                .builder()
                .memberCount(users.size())
                .totalUserNames(timeTableUtil.getUserNames())
                .availableDateTimes(timeTableUtil.getAvailableDatesDtoList())
                .build();
    }

    public IsFixedMeetingResponseDto getIsFixedMeeting(Long meetingId) throws ConflictException {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (meeting.getMonth() != null) {
            throw new ConflictException(Error.MEETING_VALIDATION_FAILED_EXCEPTION);
        }

        return IsFixedMeetingResponseDto.builder()
                .isFixed(true)
                .build();
    }

    public BestMeetingTimeResponseDto getBestMeetingTime(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        if (!meeting.getHost().getId().equals(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
        List<MeetingTimeVo> meetingTimes = new ArrayList<>();
        for (User user : meeting.getUsers()) {
            meetingTimes.addAll(
                    meetingTimeRepository.findByUser(user)
                            .stream()
                            .map(MeetingTimeVo::of)
                            .collect(Collectors.toList())
            );
        }
        MeetingVo meetingVo = MeetingVo.of(meeting);
        bestMeetingUtil.getBestMeetingTime(meetingVo, meetingTimes);
        return BestMeetingTimeResponseDto.of(meeting.getUsers().size(), bestMeetingUtil.getFixedMeetingTime());
    }

    private void isDuplicatedTime(List<PreferTimeSaveRequestDto> requestDtoList) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (PreferTimeSaveRequestDto requestDto : requestDtoList) {
            List<TimeSlot> timeSlotList = TimeSlot.getTimeSlots(requestDto.getStartTime().ordinal(), requestDto.getEndTime().ordinal() - 1);
            if (timeSlots.stream().anyMatch(timeSlotList::contains)) {
                throw new BadRequestException(Error.DUPLICATED_TIME_EXCEPTION);
            }
            timeSlots.addAll(timeSlotList);
        }
    }
}
