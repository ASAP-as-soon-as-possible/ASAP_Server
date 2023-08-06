package com.asap.server.service.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateAvailabilityVo {
    private String month;
    private String day;
    private String dayOfWeek;

    public static DateAvailabilityVo of(DateAvailability dateAvailability) {
        return new DateAvailabilityVo(dateAvailability.getMonth(), dateAvailability.getDay(), dateAvailability.getDayOfWeek());
    }
}
