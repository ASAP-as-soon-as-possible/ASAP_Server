package com.asap.server.common.dto;

import com.asap.server.exception.Error;
import com.asap.server.exception.Success;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public static SuccessResponse success(Success success) {
        return new SuccessResponse<>(success.getHttpStatusCode(), success.getMessage());
    }

    public static <T> SuccessResponse success(Error error, T data) {
        return new SuccessResponse<T>(error.getHttpStatusCode(), error.getMessage(), data);
    }

    public static <T> SuccessResponse<T> success(Success success, T data) {
        return new SuccessResponse<T>(success.getHttpStatusCode(), success.getMessage(), data);
    }
}
