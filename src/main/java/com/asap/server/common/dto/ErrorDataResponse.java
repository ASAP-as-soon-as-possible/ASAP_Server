package com.asap.server.common.dto;

import com.asap.server.exception.Error;

public class ErrorDataResponse<T> extends ErrorResponse {

    public T data;
    private ErrorDataResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> ErrorDataResponse<T> error(Error error, String message, T data) {
        return new ErrorDataResponse<T>(error.getHttpStatusCode(), message, data);
    }
}
