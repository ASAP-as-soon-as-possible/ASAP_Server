package com.asap.server.service.time.dto.retrieve;

import java.util.List;

public record TimeSlotRetrieveDto(
        String time,
        List<String> userNames,
        int colorLevel
) {
}