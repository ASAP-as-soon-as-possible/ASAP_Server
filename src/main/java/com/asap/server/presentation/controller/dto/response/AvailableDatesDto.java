package com.asap.server.presentation.controller.dto.response;

import com.asap.server.service.time.dto.retrieve.AvailableDatesRetrieveDto;
import java.util.List;

public record AvailableDatesDto(
        String month,
        String day,
        String dayOfWeek,
        List<TimeSlotDto> timeSlots
) {
    public static List<AvailableDatesDto> of(final List<AvailableDatesRetrieveDto> retrieveDtos) {
        return retrieveDtos.stream().map(
                dto -> new AvailableDatesDto(
                        dto.month(),
                        dto.day(),
                        dto.dayOfWeek(),
                        TimeSlotDto.of(dto.timeSlots())
                )).toList();
    }
}
