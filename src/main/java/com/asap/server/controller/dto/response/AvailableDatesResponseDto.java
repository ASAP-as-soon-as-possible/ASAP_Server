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
public class AvailableDatesResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private List<TimeBlockResponseDto> timeSlots;

    public static AvailableDatesResponseDto of(LocalDate date, List<TimeBlockResponseDto> timeSlotDtos) {
        return new AvailableDatesResponseDto(
                String.valueOf(date.getMonthValue()),
                String.valueOf(date.getDayOfMonth()),
                DayOfWeekConverter.convertDayOfWeekEnToKo(date.getDayOfWeek()),
                timeSlotDtos);
    }
}
