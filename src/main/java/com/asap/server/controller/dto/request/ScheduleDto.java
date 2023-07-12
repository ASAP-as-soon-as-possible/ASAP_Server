package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleDto{
    private TimeSlot startTime;
    private TimeSlot endTime;
    private int priority;
}