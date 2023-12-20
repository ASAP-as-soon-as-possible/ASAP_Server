package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;
import com.querydsl.core.annotations.QueryProjection;
import org.jetbrains.annotations.NotNull;

public record TimeBlockVo(
        int weight,
        TimeSlot timeSlot,
        Long userCount
) implements Comparable<TimeBlockVo> {
    @QueryProjection
    public TimeBlockVo {
    }

    @Override
    public int compareTo(@NotNull TimeBlockVo o) {
        return Integer.compare(this.timeSlot.ordinal(), o.timeSlot.ordinal());
    }
}
