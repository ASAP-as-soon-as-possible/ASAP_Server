package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class BadRequestException extends AsapException {
    public BadRequestException(Error error, String message) {
        super(error, message);
    }
}
