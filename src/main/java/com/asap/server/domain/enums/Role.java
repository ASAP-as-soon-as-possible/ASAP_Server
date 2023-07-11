package com.asap.server.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {
    HOST("HOST"),
    MEMBER("MEMBER");

    @JsonValue
    @Getter
    private String role;
}
