package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class BadRequestException extends AsapException {
    public BadRequestException(Error error) {
        super(error);
    }
}
