package com.asap.server.service;

import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.PreferTimeV2;
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
                       final List<PreferTimeSaveRequestDto> saveRequestDtoList) {
        saveRequestDtoList.stream()
                .map(preferTime -> preferTimeV2Repository.save(PreferTimeV2.of(meeting,
                        preferTime.getStartTime(),
                        preferTime.getEndTime())))
                .collect(Collectors.toList());
    }
}
