package com.asap.server.common.dto;

import com.asap.server.exception.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public static ErrorResponse error(Error error) {
        return new ErrorResponse(error.getHttpStatusCode(), error.getMessage());
    }

    public static ErrorResponse error(Error error, String message) {
        return new ErrorResponse(error.getHttpStatusCode(), message);
    }

    public static <T> ErrorResponse<T> error(Error error, String message, T data) {
        return new ErrorResponse<>(error.getHttpStatusCode(), error.getMessage(), data);
    }
}
