package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TimeBlockResponseDto {
    private String time;
    private List<String> userNames;
    private int colorLevel;

    public static TimeBlockResponseDto of(String time,
                                          List<String> userNames,
                                          int colorLevel) {
        return new TimeBlockResponseDto(time, userNames, colorLevel);
    }
}
