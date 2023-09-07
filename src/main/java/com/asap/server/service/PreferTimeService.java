package com.asap.server.service;

import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.controller.dto.response.PreferTimeResponseDto;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.PreferTimeV2;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.PreferTimeV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferTimeService {
    private final PreferTimeV2Repository preferTimeV2Repository;

    public void create(final MeetingV2 meeting,
                       final List<PreferTimeSaveRequestDto> saveRequestDtos) {
        saveRequestDtos.stream()
                .map(preferTime -> preferTimeV2Repository.save(
                        PreferTimeV2.builder()
                                .meeting(meeting)
                                .startTime(preferTime.getStartTime())
                                .endTime(preferTime.getEndTime()).build()))
                .collect(Collectors.toList());
    }

    public List<PreferTimeResponseDto> getPreferTimes(final MeetingV2 meetingV2) {
        List<PreferTimeV2> preferTimeV2s = preferTimeV2Repository.findByMeeting(meetingV2)
                .orElseThrow(() -> new NotFoundException(Error.PREFER_TIME_NOT_FOUND_EXCEPTION));

        return preferTimeV2s.stream()
                .map(preferTime -> PreferTimeResponseDto.builder()
                        .startTime(preferTime.getStartTime().getTime())
                        .endTime(preferTime.getEndTime().getTime())
                        .build())
                .collect(Collectors.toList());
    }
}
