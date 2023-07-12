package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.AvailableDateResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.domain.DateAvailability;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import com.asap.server.domain.User;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.DateAvailabilityRepository;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.PreferTimeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import static com.asap.server.exception.Error.INVALID_MEETING_HOST_EXCEPTION;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final DateAvailabilityRepository dateAvailabilityRepository;
    private final PreferTimeRepository preferTimeRepository;
    private final JwtService jwtService;

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
                .map(preferTimeSaveRequestDto -> PreferTime.newInstance(preferTimeSaveRequestDto.getStartTime(), preferTimeSaveRequestDto.getEndTime()))
                .collect(Collectors.toList());
        preferTimeRepository.saveAllAndFlush(preferTimeList);
        User host = userService.createHost(meetingSaveRequestDto.getName());
        Meeting newMeeting = Meeting.newInstance(
                host,
                dateAvailabilityList,
                preferTimeList,
                meetingSaveRequestDto.getPassword(),
                meetingSaveRequestDto.getTitle(),
                meetingSaveRequestDto.getPlace(),
                meetingSaveRequestDto.getPlaceDetail(),
                meetingSaveRequestDto.getDuration(),
                meetingSaveRequestDto.getAdditionalInfo());
        meetingRepository.save(newMeeting);
        String accessToken = jwtService.issuedToken(host.getId().toString());
        newMeeting.setUrl(Base64Utils.encodeToUrlSafeString(newMeeting.getId().toString().getBytes()));
        return new MeetingSaveResponseDto(newMeeting.getUrl(), accessToken);
    }

    @Transactional
    public void confirmMeeting(
            MeetingConfirmRequestDto meetingConfirmRequestDto,
            Long userId,
            Long meetingId
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
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponseDto getMeetingSchedule(Long meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        List<AvailableDateResponseDto> availableDateResponseDtoList = meeting.getDateAvailabilities()
                .stream()
                .map(dateAvailability -> new AvailableDateResponseDto(
                        dateAvailability.getMonth(),
                        dateAvailability.getDay(),
                        dateAvailability.getDayOfWeek()))
                .collect(Collectors.toList());
        List<PreferTimeResponseDto> preferTimeResponseDtoList = meeting.getPreferTimes()
                .stream()
                .map(preferTime -> new PreferTimeResponseDto(
                        preferTime.getStartTime().getTime(),
                        preferTime.getEndTime().getTime()
                ))
                .collect(Collectors.toList());
        return new MeetingScheduleResponseDto(
                meeting.getDuration(),
                meeting.getPlace(),
                meeting.getPlaceDetail(),
                availableDateResponseDtoList,
                preferTimeResponseDtoList);
    }
}
