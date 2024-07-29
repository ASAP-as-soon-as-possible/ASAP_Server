package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class UnauthorizedException extends AsapException {
    public UnauthorizedException(Error error) {
        super(error);
    }
}
