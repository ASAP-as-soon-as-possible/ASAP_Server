package com.asap.server.presentation.controller.dto.response;

import com.asap.server.service.time.dto.retrieve.TimeTableRetrieveDto;
import java.util.List;

public record TimeTableResponseDto(
        int memberCount,
        List<String> totalUserNames,
        List<AvailableDatesDto> availableDateTimes
) {
    public static TimeTableResponseDto of(final TimeTableRetrieveDto retrieveDto) {
        return new TimeTableResponseDto(
                retrieveDto.memberCount(),
                retrieveDto.totalUserNames(),
                AvailableDatesDto.of(retrieveDto.availableDateTimes()));
    }
}
