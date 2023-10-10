package com.asap.server.common.dto;

import com.asap.server.exception.Error;
import lombok.Getter;

@Getter
public class ErrorDataResponse<T> extends ErrorResponse {

    private T data;

    private ErrorDataResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> ErrorDataResponse<T> error(Error error, String message, T data) {
        return new ErrorDataResponse<T>(error.getHttpStatusCode(), message, data);
    }

    @Override
    public String toString() {
        return "code: " + super.getCode() + " message: " + super.getMessage() + " data: " + this.data;
    }
}
