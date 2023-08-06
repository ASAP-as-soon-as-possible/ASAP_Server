package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AvailableDateResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;
}
