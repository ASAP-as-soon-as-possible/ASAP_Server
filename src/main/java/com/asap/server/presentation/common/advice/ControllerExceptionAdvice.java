package com.asap.server.presentation.common.advice;

import com.asap.server.presentation.common.dto.ErrorDataResponse;
import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.infra.slack.SlackUtil;
import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.AsapException;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.ForbiddenException;
import com.asap.server.common.exception.model.HostTimeForbiddenException;
import com.asap.server.common.exception.model.InternalErrorException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.exception.model.TooManyRequestException;
import com.asap.server.common.exception.model.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.time.DateTimeException;

import static com.asap.server.common.exception.Error.METHOD_NOT_ALLOWED_EXCEPTION;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionAdvice {

    private final SlackUtil slackUtil;

    /**
     * 400 Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    protected ErrorResponse handleValidException(final ValidationException e) {
        return ErrorResponse.error(Error.VALIDATION_REQUEST_MISSING_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeException.class)
    protected ErrorResponse handleDateTimeException(final DateTimeException e) {
        return ErrorResponse.error(Error.INVALID_DATE_FORMAT_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) return ErrorResponse.error(Error.VALIDATION_REQUEST_MISSING_EXCEPTION);
        else return ErrorResponse.error(Error.VALIDATION_REQUEST_MISSING_EXCEPTION, fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ErrorResponse handleJsonParseException(final HttpMessageNotReadableException e) {
        return ErrorResponse.error(Error.INVALID_JSON_INPUT_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ErrorResponse handleValidationException(final ConstraintViolationException e) {
        return ErrorResponse.error(Error.VALIDATION_REQUEST_MISSING_EXCEPTION);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ErrorResponse handleBadRequestException(final BadRequestException e) {
        return ErrorResponse.error(e.getError());
    }

    /**
     * 401 UnAuthorization
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    protected ErrorResponse handleUnAuthorizedException(final UnauthorizedException e) {
        return ErrorResponse.error(e.getError());
    }

    /**
     * 403 Forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    protected ErrorResponse handleForbiddenException(final ForbiddenException e) {
        return ErrorResponse.error(e.getError());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HostTimeForbiddenException.class)
    protected ErrorDataResponse<HostLoginResponseDto> handleForbiddenException(final HostTimeForbiddenException e) {
        HostLoginResponseDto body = new HostLoginResponseDto(e.getData());
        return ErrorDataResponse.error(e.getError(), e.getMessage(), body);
    }

    /**
     * 404 Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ErrorResponse handleNotFoundException(final NotFoundException e) {
        return ErrorResponse.error(e.getError());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResponse handleNotFoundException(NoHandlerFoundException exception) {
        return ErrorResponse.error(Error.URI_NOT_FOUND_EXCEPTION);
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return ErrorResponse.error(METHOD_NOT_ALLOWED_EXCEPTION);
    }

    /**
     * 409 Conflict
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    protected ErrorResponse handleConflictException(final ConflictException e) {
        return ErrorResponse.error(e.getError());
    }

    /*
     * 429 Too Many Requests
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(TooManyRequestException.class)
    protected ErrorResponse handleTooManyConflictException(final TooManyRequestException e) {
        return ErrorResponse.error(e.getError());
    }

    /**
     * 500 Internal Server
     */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    protected ErrorResponse handleInternalErrorException(final InternalErrorException e) {
        return ErrorResponse.error(e.getError());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(final Exception error, final HttpServletRequest request) throws IOException {
        log.error("================================================NEW===============================================");
        log.error(error.getMessage(), error);
        slackUtil.sendAlert(error, request);
        return ErrorResponse.error(Error.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsapException.class)
    protected ErrorResponse handleAsapException(final AsapException e) {
        return ErrorResponse.error(e.getError());
    }
}
