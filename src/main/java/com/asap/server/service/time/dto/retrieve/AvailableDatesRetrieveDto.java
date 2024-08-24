package com.asap.server.service.time.dto.retrieve;

import com.asap.server.common.utils.DateUtil;

import java.time.LocalDate;
import java.util.List;

public record AvailableDatesRetrieveDto(
        String month,
        String day,
        String dayOfWeek,
        List<TimeBlockRetrieveDto> timeSlots
) {
    public static AvailableDatesRetrieveDto of(final LocalDate date, final List<TimeBlockRetrieveDto> timeSlots) {
        return new AvailableDatesRetrieveDto(
                DateUtil.getMonth(date),
                DateUtil.getDay(date),
                DateUtil.getDayOfWeek(date),
                timeSlots);
    }
}
