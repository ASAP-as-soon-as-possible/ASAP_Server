package com.asap.server.presentation.controller.dto.response;

import com.asap.server.service.time.dto.retrieve.TimeBlockRetrieveDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TimeSlotDto (
        String time,
        List<String> userNames,
        int colorLevel
) {
    public static List<TimeSlotDto> of(List<TimeBlockRetrieveDto> retrieveDtos) {
        return retrieveDtos.stream().map(
                dto -> new TimeSlotDto(dto.time(), dto.userNames(), dto.colorLevel())
        ).toList();
    }
}