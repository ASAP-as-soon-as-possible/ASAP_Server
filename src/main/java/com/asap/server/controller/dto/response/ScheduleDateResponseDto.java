package com.asap.server.controller.dto.response;

import com.asap.server.common.utils.DayOfWeekConverter;
import com.asap.server.domain.AvailableDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleDateResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;

    public static ScheduleDateResponseDto of(AvailableDate availableDate){
        return new ScheduleDateResponseDto(
                String.valueOf(availableDate.getDate().getMonthValue()),
                String.valueOf(availableDate.getDate().getDayOfMonth()),
                DayOfWeekConverter.convertDayOfWeekEnToKo(availableDate.getDate().getDayOfWeek())
        );
    }
}
