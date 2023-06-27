package com.asap.server.common.advice;

import com.asap.server.common.dto.ApiResponse;
import com.asap.server.exception.model.AsapException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(AsapException.class)
    protected ApiResponse handleAsapException(final AsapException e){
        return ApiResponse.error(e.getError(), e.getMessage());
    }
}
