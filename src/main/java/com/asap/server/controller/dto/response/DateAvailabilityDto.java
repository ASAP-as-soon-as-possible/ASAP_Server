package com.asap.server.controller.dto.response;

import com.asap.server.domain.DateAvailability;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateAvailabilityDto {
    private String month;
    private String day;
    private String dayOfWeek;

    public static DateAvailabilityDto of(DateAvailability dateAvailability) {
        return new DateAvailabilityDto(dateAvailability.getMonth(), dateAvailability.getDay(), dateAvailability.getDayOfWeek());
    }
}
