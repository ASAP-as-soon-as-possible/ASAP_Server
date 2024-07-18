package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class TooManyRequestException extends AsapException {

    public TooManyRequestException(final Error error) {
        super(error);
    }
}
