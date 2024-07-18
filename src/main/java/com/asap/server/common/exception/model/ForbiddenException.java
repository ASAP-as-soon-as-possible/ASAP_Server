package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class ForbiddenException extends AsapException {
    public ForbiddenException(Error error) {
        super(error);
    }
}
