package com.asap.server.service;

import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.PreferTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferTimeService {
    private final PreferTimeRepository preferTimeRepository;

    public void create(final Meeting meeting,
                       final List<PreferTimeSaveRequestDto> saveRequestDtos) {
        saveRequestDtos.stream()
                .map(preferTime -> preferTimeRepository.save(
                        PreferTime.builder()
                                .meeting(meeting)
                                .startTime(preferTime.getStartTime())
                                .endTime(preferTime.getEndTime()).build()))
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
}
