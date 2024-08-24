package com.asap.server.service.time.dto.register;

import com.asap.server.persistence.domain.enums.TimeSlot;

public record UserMeetingScheduleRegisterDto(
        String month,
        String day,
        TimeSlot startTime,
        TimeSlot endTime,
        int priority
) {
}
