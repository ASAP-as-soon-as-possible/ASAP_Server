package com.asap.server.persistence.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {
    HOST("HOST"),
    MEMBER("MEMBER");

    @Getter
    @JsonValue
    private String role;
}
