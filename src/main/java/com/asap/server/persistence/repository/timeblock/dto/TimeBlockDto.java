package com.asap.server.persistence.repository.timeblock.dto;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.querydsl.core.annotations.QueryProjection;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record TimeBlockDto(
        LocalDate availableDate,
        TimeSlot timeSlot,
        int weight,
        Long userCount
) implements Comparable<TimeBlockDto> {
    @QueryProjection
    public TimeBlockDto {
    }

    @Override
    public int compareTo(@NotNull TimeBlockDto o) {
        if (this.availableDate.equals(o.availableDate))
            return Integer.compare(this.timeSlot.ordinal(), o.timeSlot.ordinal());
        return this.availableDate.compareTo(o.availableDate);
    }
}
