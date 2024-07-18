package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class NotFoundException extends AsapException {
    public NotFoundException(Error error) {
        super(error);
    }
}
