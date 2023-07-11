package com.asap.server.service;

import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.domain.DateAvailability;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import com.asap.server.domain.User;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.DateAvailabilityRepository;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.PreferTimeRepository;
import com.asap.server.repository.UserRepository;
import com.asap.server.exception.Error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final DateAvailabilityRepository dateAvailabilityRepository;
    private final PreferTimeRepository preferTimeRepository;

    @Transactional
    public String create(MeetingSaveRequestDto meetingSaveRequestDto){
        List<DateAvailability> dateAvailabilityList = meetingSaveRequestDto
                .getAvailableDateList()
                .stream()
                .map(s -> DateAvailability.newInstance(s))
                .collect(Collectors.toList());
        dateAvailabilityRepository.saveAllAndFlush(dateAvailabilityList);
        List<PreferTime> preferTimeList = meetingSaveRequestDto
                .getPreferTimeSaveRequestDtoList()
                .stream()
                .map(PreferTimeSaveRequestDto -> PreferTime.newInstance(PreferTimeSaveRequestDto.getStartTime(), PreferTimeSaveRequestDto.getEndTime()))
                .collect(Collectors.toList());
        preferTimeRepository.saveAllAndFlush(preferTimeList);
        User host = userRepository.findByName(meetingSaveRequestDto.getName()).orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND_EXCEPTION));
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
        Meeting meeting = meetingRepository.findById(newMeeting.getId()).orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        meeting.setUrl(Base64Utils.encodeToUrlSafeString(meeting.getId().toString().getBytes()));
        meetingRepository.save(meeting);
        return newMeeting.getUrl();
    }
}
