package com.asap.server.common.advice;

import com.asap.server.common.dto.ErrorResponse;
import com.asap.server.common.utils.SlackUtil;
import com.asap.server.exception.model.AsapException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import com.asap.server.exception.Error;
import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionAdvice {

    private final SlackUtil slackUtil;
    /**
     * 400 Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(final ValidationException e) {
        return ErrorResponse.error(Error.VALIDATION_REQUEST_MISSING_EXCEPTION);
    }

    /**
     * 500 Internal Server
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(final Exception error, final HttpServletRequest request) throws IOException {
        slackUtil.sendAlert(error,request);
        return ErrorResponse.error(Error.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsapException.class)
    protected ErrorResponse handleAsapException(final AsapException e){
        return ErrorResponse.error(e.getError(), e.getMessage());
    }
}
