package com.asap.server.common.dto;

import com.asap.server.exception.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private final int code;
    private final String message;

    public static ErrorResponse error(Error error) {
        return new ErrorResponse(error.getHttpStatusCode(), error.getMessage());
    }

    public static ErrorResponse error(Error error, String message) {
        return new ErrorResponse(error.getHttpStatusCode(), message);
    }
}
