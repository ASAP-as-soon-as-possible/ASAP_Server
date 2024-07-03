package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class InternalErrorException extends AsapException {
    public InternalErrorException(final Error error) {
        super(error);
    }
}
