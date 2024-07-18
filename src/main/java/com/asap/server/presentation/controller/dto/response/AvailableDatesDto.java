package com.asap.server.presentation.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class AvailableDatesDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private List<TimeSlotDto> timeSlots;
}
