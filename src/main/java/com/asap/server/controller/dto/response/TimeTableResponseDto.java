package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
@Getter
@Builder
@ToString
@AllArgsConstructor
public class TimeTableResponseDto {
    public int memberCount;
    public List<String> totalUserNames;
    private List<AvailableDatesDto> availableDateTimes;
}
