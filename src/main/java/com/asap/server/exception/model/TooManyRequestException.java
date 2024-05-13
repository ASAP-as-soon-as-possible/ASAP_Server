package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class TooManyRequestException extends AsapException {

    public TooManyRequestException(final Error error) {
        super(error);
    }
}
