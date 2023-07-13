package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AvailableDatesDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private List<TimeSlotDto> timeSlots;
}
