package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TimeTableResponseDto {
    public int memberCount;
    public List<String> totalUserNames;
    private List<AvailableDatesResponseDto> availableDateTimes;

    public static TimeTableResponseDto of(
            int memberCount,
            List<String> totalUserNames,
            List<AvailableDatesResponseDto> availableDateTimes
    ) {
        return new TimeTableResponseDto(memberCount, totalUserNames, availableDateTimes);
    }
}
