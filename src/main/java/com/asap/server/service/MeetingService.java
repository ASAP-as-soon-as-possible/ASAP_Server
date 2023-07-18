package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.AvailableDateResponseDto;
import com.asap.server.controller.dto.response.AvailableDatesDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.IsFixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.controller.dto.response.TimeSlotDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.domain.DateAvailability;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.PreferTime;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.DateAvailabilityRepository;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.MeetingTimeRepository;
import com.asap.server.repository.PreferTimeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final MeetingTimeRepository meetingTimeRepository;
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
    public MeetingScheduleResponseDto getMeetingSchedule(Long meetingId) {
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
                .month(meeting.getMonth())
                .day(meeting.getDay())
                .dayOfWeek(meeting.getDayOfWeek())
                .startTime(meeting.getStartTime().getTime())
                .endTime(meeting.getEndTime().getTime())
                .hostName(meeting.getHost().getName())
                .userNames(userNames)
                .additionalInfo(meeting.getAdditionalInfo())
                .build();
    }

    public TimeTableResponseDto getTimeTable(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        List<User> users = meeting.getUsers();
        List<String> userNames = new ArrayList<>();
        Map<String, Map<String, List<String>>> dateAvailable = new HashMap<>();

        for (User user : users) {
            List<MeetingTime> meetingTimes = meetingTimeRepository.findByUser(user);
            for (MeetingTime meetingTime : meetingTimes) {
                List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(meetingTime.getStartTime().ordinal(), meetingTime.getEndTime().ordinal());
                for (TimeSlot timeSlot : timeSlots) {
                    String colTime = timeSlot.getTime();
                    String col = String.format("%s %s %s", meetingTime.getMonth(), meetingTime.getDay(), meetingTime.getDayOfWeek());
                    if (dateAvailable.containsKey(col)) {
                        if (dateAvailable.get(col).containsKey(colTime)) {
                            dateAvailable.get(col).get(colTime).add(user.getName());
                        } else {
                            List<String> name = new ArrayList<>();
                            name.add(user.getName());
                            dateAvailable.get(col).put(colTime, name);
                        }
                    } else {
                        Map<String, List<String>> timeAvailable = new HashMap<>();
                        List<String> name = new ArrayList<>();
                        name.add(user.getName());
                        timeAvailable.put(colTime, name);
                        dateAvailable.put(col, timeAvailable);
                    }
                }
            }
            userNames.add(user.getName());
        }

        List<AvailableDatesDto> availableDatesDtos = new ArrayList<>();
        dateAvailable.forEach((key, value) -> {
                    List<TimeSlotDto> timeSlotDtos = new ArrayList<>();
                    value.forEach((timeSlot, userNameList) ->
                            {
                                int colorLevel;
                                if (userNameList.size() > 0 && (userNameList.size() <= users.size() * (0.2))) {
                                    colorLevel = 1;
                                } else if (userNameList.size() > users.size() * (0.2) && userNameList.size() <= users.size() * (0.4)) {
                                    colorLevel = 2;
                                } else if (userNameList.size() > users.size() * (0.4) && userNameList.size() <= users.size() * (0.6)) {
                                    colorLevel = 3;
                                } else if (userNameList.size() > users.size() * (0.6) && userNameList.size() <= users.size() * (0.8)) {
                                    colorLevel = 4;
                                } else if (userNameList.size() > users.size() * (0.8) && userNameList.size() <= users.size()) {
                                    colorLevel = 5;
                                } else {
                                    colorLevel = 0;
                                }
                                timeSlotDtos.add(TimeSlotDto
                                        .builder()
                                        .time(timeSlot)
                                        .userNames(userNameList)
                                        .colorLevel(colorLevel)
                                        .build());
                            }
                    );
                    Collections.sort(timeSlotDtos, Comparator.comparing(TimeSlotDto::getTime));
                    String month = Integer.valueOf(key.substring(0, 2)).toString();
                    String day = Integer.valueOf(key.substring(3, 5)).toString();
                    String dayOfWeek = key.substring(6, 7);

                    availableDatesDtos.add(AvailableDatesDto
                            .builder()
                            .month(month)
                            .day(day)
                            .dayOfWeek(dayOfWeek)
                            .timeSlots(timeSlotDtos)
                            .build()
                    );
                }
        );

        return TimeTableResponseDto
                .builder()
                .memberCount(users.size())
                .totalUserNames(userNames)
                .availableDateTimes(availableDatesDtos)
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
}
