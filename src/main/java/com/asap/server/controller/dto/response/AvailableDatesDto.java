package com.asap.server.controller.dto.response;

import com.asap.server.common.utils.DayOfWeekConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AvailableDatesDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private List<TimeSlotDto> timeSlots;

    public static AvailableDatesDto of(LocalDate date, List<TimeSlotDto> timeSlotDtos) {
        return new AvailableDatesDto(
                String.valueOf(date.getMonthValue()),
                String.valueOf(date.getDayOfMonth()),
                DayOfWeekConverter.convertDayOfWeekEnToKo(date.getDayOfWeek()),
                timeSlotDtos);
    }
}
