package com.asap.server.service;

import com.asap.server.common.utils.BestMeetingUtil;
import com.asap.server.common.utils.TimeTableUtil;
import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.AvailableDateResponseDto;
import com.asap.server.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.IsFixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.domain.DateAvailability;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import com.asap.server.domain.User;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import com.asap.server.repository.DateAvailabilityRepository;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.MeetingTimeRepository;
import com.asap.server.repository.PreferTimeRepository;
import com.asap.server.service.vo.MeetingTimeVo;
import com.asap.server.service.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.asap.server.exception.Error.INVALID_MEETING_HOST_EXCEPTION;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingTimeRepository meetingTimeRepository;
    private final UserService userService;
    private final DateAvailabilityRepository dateAvailabilityRepository;
    private final PreferTimeRepository preferTimeRepository;
    private final JwtService jwtService;
    private final BestMeetingUtil bestMeetingUtil;
    private final TimeTableUtil timeTableUtil;

    @Transactional
    public MeetingSaveResponseDto create(MeetingSaveRequestDto meetingSaveRequestDto) {

        List<DateAvailability> dateAvailabilityList = meetingSaveRequestDto
                .getAvailableDates()
                .stream()
                .map(s -> DateAvailability.newInstance(s))
                .collect(Collectors.toList());
        dateAvailabilityRepository.saveAllAndFlush(dateAvailabilityList);

        List<PreferTime> preferTimeList = meetingSaveRequestDto
                .getPreferTimes()
                .stream()
                .map(
                        preferTimeSaveRequestDto ->
                                PreferTime.newInstance(
                                        preferTimeSaveRequestDto.getStartTime(),
                                        preferTimeSaveRequestDto.getEndTime()
                                )
                )
                .collect(Collectors.toList());
        preferTimeRepository.saveAllAndFlush(preferTimeList);

        User host = userService.createHost(meetingSaveRequestDto.getName());
        List<User> users = new ArrayList<>();
        users.add(host);

        Meeting newMeeting = Meeting.newInstance(
                host,
                dateAvailabilityList,
                preferTimeList,
                users,
                meetingSaveRequestDto.getPassword(),
                meetingSaveRequestDto.getTitle(),
                meetingSaveRequestDto.getPlace(),
                meetingSaveRequestDto.getPlaceDetail(),
                meetingSaveRequestDto.getDuration(),
                meetingSaveRequestDto.getAdditionalInfo());
        meetingRepository.save(newMeeting);

        String accessToken = jwtService.issuedToken(host.getId().toString());
        newMeeting.setUrl(Base64Utils.encodeToUrlSafeString(newMeeting.getId().toString().getBytes()));

        return MeetingSaveResponseDto.builder()
                .url(newMeeting.getUrl())
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
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        List<AvailableDateResponseDto> availableDateResponseDtoList = meeting.getDateAvailabilities()
                .stream()
                .map(dateAvailability -> new AvailableDateResponseDto(
                        Integer.valueOf(dateAvailability.getMonth()).toString(),
                        Integer.valueOf(dateAvailability.getDay()).toString(),
                        dateAvailability.getDayOfWeek()))
                .collect(Collectors.toList());

        List<PreferTimeResponseDto> preferTimeResponseDtoList = meeting.getPreferTimes()
                .stream()
                .map(preferTime -> new PreferTimeResponseDto(
                        preferTime.getStartTime().getTime(),
                        preferTime.getEndTime().getTime()
                ))
                .sorted(Comparator.comparing(PreferTimeResponseDto::getStartTime))
                .collect(Collectors.toList());

        return MeetingScheduleResponseDto.builder()
                .duration(meeting.getDuration())
                .place(meeting.getPlace())
                .placeDetail(meeting.getPlaceDetail())
                .availableDates(availableDateResponseDtoList)
                .preferTimes(preferTimeResponseDtoList)
                .build();
    }

    public FixedMeetingResponseDto getFixedMeetingInformation(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        List<String> userNames = meeting
                .getUsers()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());

        return FixedMeetingResponseDto
                .builder()
                .title(meeting.getTitle())
                .place(meeting.getPlace().toString())
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
            List<MeetingTimeVo> meetingTimes = meetingTimeRepository.findByUser(user)
                    .stream()
                    .map(MeetingTimeVo::of)
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
        List<MeetingTimeDto> meetingTimes = new ArrayList<>();
        for (User user : meeting.getUsers()) {
            meetingTimes.addAll(
                    meetingTimeRepository.findByUser(user)
                            .stream()
                            .map(MeetingTimeDto::of)
                            .collect(Collectors.toList())
            );
        }
        MeetingDto meetingDto = MeetingDto.of(meeting);
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);
        return BestMeetingTimeResponseDto.of(meeting.getUsers().size(), bestMeetingUtil.getFixedMeetingTime());
    }
}
