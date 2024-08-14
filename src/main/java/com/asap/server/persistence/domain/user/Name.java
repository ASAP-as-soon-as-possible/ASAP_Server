package com.asap.server.persistence.domain.user;

import static com.asap.server.common.exception.Error.USERNAME_NOT_BLANK_EXCEPTION;
import static com.asap.server.common.exception.Error.USERNAME_NOT_NULL_EXCEPTION;
import static com.asap.server.common.exception.Error.USERNAME_TOO_LONG_EXCEPTION;

import com.asap.server.common.exception.model.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Name {
    @Column(nullable = false, name = "name")
    private String value;

    private static final int MAX_NAME_LENGTH = 8;

    public Name(String value) {
        if (value == null) {
            throw new BadRequestException(USERNAME_NOT_NULL_EXCEPTION);
        }
        if (value.isBlank()) {
            throw new BadRequestException(USERNAME_NOT_BLANK_EXCEPTION);
        }
        if (value.trim().length() > MAX_NAME_LENGTH) {
            throw new BadRequestException(USERNAME_TOO_LONG_EXCEPTION);
        }

        this.value = value.trim();
    }
}

