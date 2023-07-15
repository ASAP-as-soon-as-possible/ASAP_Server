package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingTimeResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
