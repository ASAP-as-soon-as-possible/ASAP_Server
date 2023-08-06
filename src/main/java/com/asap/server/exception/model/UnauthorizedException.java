package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class UnauthorizedException extends AsapException {
    public UnauthorizedException(Error error) {
        super(error);
    }
}
