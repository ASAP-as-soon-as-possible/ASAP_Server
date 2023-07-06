package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class NotFoundException extends AsapException {
    public NotFoundException(Error error, String message) {
        super(error, message);
    }
}
