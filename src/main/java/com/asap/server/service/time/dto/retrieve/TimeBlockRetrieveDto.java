package com.asap.server.service.time.dto.retrieve;

import java.util.List;

public record TimeBlockRetrieveDto(
        String time,
        List<String> userNames,
        int colorLevel
) {
}