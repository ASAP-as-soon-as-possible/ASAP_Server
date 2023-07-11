package com.asap.server.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Place {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    UNDEFINED("UNDEFINED");

    @Getter
    @JsonValue
    private String place;
}
