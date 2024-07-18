package com.asap.server.service;

import com.asap.server.presentation.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.presentation.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.PreferTime;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.persistence.repository.PreferTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferTimeService {
    private final PreferTimeRepository preferTimeRepository;

    public void create(final Meeting meeting,
                       final List<PreferTimeSaveRequestDto> saveRequestDtos) {
        if (isPreferTimeDuplicated(saveRequestDtos)) {
            throw new BadRequestException(Error.DUPLICATED_TIME_EXCEPTION);
        }
        saveRequestDtos.stream()
                .sorted(Comparator.comparing(preferTime -> preferTime.startTime().getTime()))
                .map(preferTime -> preferTimeRepository.save(
                        PreferTime.builder()
                                .meeting(meeting)
                                .startTime(preferTime.startTime())
                                .endTime(preferTime.endTime()).build()))
                .collect(Collectors.toList());
    }

    public List<PreferTimeResponseDto> getPreferTimes(final Meeting meeting) {
        List<PreferTime> preferTimes = preferTimeRepository.findByMeeting(meeting);
        if (preferTimes.isEmpty()) throw new NotFoundException(Error.PREFER_TIME_NOT_FOUND_EXCEPTION);

        return preferTimes.stream()
                .map(preferTime -> PreferTimeResponseDto.builder()
                        .startTime(preferTime.getStartTime().getTime())
                        .endTime(preferTime.getEndTime().getTime())
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isPreferTimeDuplicated(List<PreferTimeSaveRequestDto> requestDtos) {
        List<TimeSlot> timeSlots = requestDtos.stream()
                .flatMap(requestDto -> TimeSlot.getTimeSlots(requestDto.startTime().ordinal(), requestDto.endTime().ordinal() - 1).stream())
                .collect(Collectors.toList());
        return timeSlots.size() != timeSlots.stream().distinct().count();
    }
}
