package com.asap.server.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Duration {
    HALF("HALF"),
    HOUR("HOUR"),
    HOUR_HALF("HOUR_HALF"),
    TWO_HOUR("TWO_HOUR"),
    TWO_HOUR_HALF("TWO_HOUR_HALF"),
    THREE_HOUR("THREE_HOUR");

    @JsonValue
    @Getter
    private String duration;
}