package com.asap.server.service;

import com.asap.server.common.utils.BestMeetingUtil;
import com.asap.server.common.utils.TimeTableUtil;
import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.IsFixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.Place;
import com.asap.server.domain.PreferTime;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.service.vo.MeetingTimeVo;
import com.asap.server.service.vo.MeetingVo;
import com.asap.server.service.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final AvailableDateService availableDateService;
    private final PreferTimeService preferTimeService;
    private final JwtService jwtService;
    private final BestMeetingUtil bestMeetingUtil;
    private final TimeTableUtil timeTableUtil;

    @Transactional
    public MeetingSaveResponseDto create(MeetingSaveRequestDto meetingSaveRequestDto) {

        List<AvailableDate> availableDates = availableDateService.create(meetingSaveRequestDto.getAvailableDates());

        isDuplicatedTime(meetingSaveRequestDto.getPreferTimes());
        List<PreferTime> preferTimes = preferTimeService.create(meetingSaveRequestDto.getPreferTimes());

        User host = userService.createHost(meetingSaveRequestDto.getName());
        Meeting newMeeting = createMeeting(host, availableDates, preferTimes, meetingSaveRequestDto);

        String accessToken = jwtService.issuedToken(host.getId().toString());
        newMeeting.setUrl(Base64Utils.encodeToUrlSafeString(newMeeting.getId().toString().getBytes()));

        return MeetingSaveResponseDto.builder()
                .url(newMeeting.getUrl())
                .accessToken(accessToken)
                .build();
    }

    private Meeting createMeeting(
            User host,
            List<AvailableDate> availableDates,
            List<PreferTime> preferTimes,
            MeetingSaveRequestDto requestDto) {

        List<User> users = new ArrayList<>();
        users.add(host);

        Meeting newMeeting = Meeting.newInstance(
                host,
                availableDates,
                preferTimes,
                users,
                requestDto.getPassword(),
                requestDto.getTitle(),
                new Place(requestDto.getPlaceType(), requestDto.getPlaceDetail()),
                requestDto.getDuration(),
                requestDto.getAdditionalInfo());

        meetingRepository.save(newMeeting);
        return newMeeting;
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

    public MeetingScheduleResponseDto getMeetingSchedule(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        return MeetingScheduleResponseDto.builder()
                .duration(meeting.getDuration())
                .place(meeting.getPlace().getPlaceType())
                .placeDetail(meeting.getPlace().getPlaceDetail())
                .availableDates(availableDateService.getAvailableDate(meeting.getAvailableDates()))
                .preferTimes(preferTimeService.getPreferTime(meeting.getPreferTimes()))
                .build();
    }

    public FixedMeetingResponseDto getFixedMeetingInformation(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        return FixedMeetingResponseDto.of(meeting);
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

        if (meeting.getConfirmedTime() != null) {
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
