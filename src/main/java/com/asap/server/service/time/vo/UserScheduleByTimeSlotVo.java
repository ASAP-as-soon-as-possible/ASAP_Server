package com.asap.server.service.time.vo;

import com.asap.server.persistence.domain.enums.TimeSlot;
import java.time.LocalDate;

public record UserScheduleByTimeSlotVo(
        Long id,
        LocalDate availableDate,
        Long userId,
        TimeSlot time,
        int weight
) {
    public record CompositeKey(LocalDate availableDate, TimeSlot time) {
    }

    public CompositeKey composeKey() {
        return new CompositeKey(this.availableDate, this.time);
    }
}
