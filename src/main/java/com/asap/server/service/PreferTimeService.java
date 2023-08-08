package com.asap.server.service;

import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.domain.PreferTime;
import com.asap.server.repository.PreferTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferTimeService {

    private final PreferTimeRepository preferTimeRepository;

    public List<PreferTime> create(List<PreferTimeSaveRequestDto> preferTimeDtos) {
        List<PreferTime> preferTimes =
                preferTimeDtos.stream()
                        .map(dto -> PreferTime.newInstance(dto.getStartTime(), dto.getEndTime()))
                        .collect(Collectors.toList());
        preferTimeRepository.saveAll(preferTimes);
        return preferTimes;
    }
}