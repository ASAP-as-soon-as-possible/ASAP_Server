package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateAvailabilityDto {
    private String month;
    private String day;
    private String dayOfWeek;
}
