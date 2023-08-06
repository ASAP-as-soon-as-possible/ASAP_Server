package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class NotFoundException extends AsapException {
    public NotFoundException(Error error) {
        super(error);
    }
}
