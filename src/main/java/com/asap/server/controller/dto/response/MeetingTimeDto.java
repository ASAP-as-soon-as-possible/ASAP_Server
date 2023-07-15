package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MeetingTimeDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private String name;
    private String priority;
}
