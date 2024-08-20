package com.asap.server.service.time.vo;

import com.asap.server.persistence.domain.enums.TimeSlot;
import java.time.LocalDate;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record TimeBlockVo(
        LocalDate availableDate,
        TimeSlot timeSlot,
        int weight,
        List<Long> userIds
) implements Comparable<TimeBlockVo> {
    @Override
    public int compareTo(@NotNull final TimeBlockVo o) {
        if (this.availableDate.equals(o.availableDate)) {
            return Integer.compare(this.timeSlot.getIndex(), o.timeSlot.getIndex());
        }
        return this.availableDate.compareTo(o.availableDate);
    }
}
