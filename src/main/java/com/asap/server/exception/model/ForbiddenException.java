package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class ForbiddenException extends AsapException {
    public ForbiddenException(Error error) {
        super(error);
    }
}
