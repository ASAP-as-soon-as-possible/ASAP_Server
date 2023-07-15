package com.asap.server.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Duration {
    HALF("HALF", 2),
    HOUR("HOUR", 3),
    HOUR_HALF("HOUR_HALF", 4),
    TWO_HOUR("TWO_HOUR", 5),
    TWO_HOUR_HALF("TWO_HOUR_HALF", 6),
    THREE_HOUR("THREE_HOUR", 7);


    @JsonValue
    private final String duration;
    private final int needBlock;
}