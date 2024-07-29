package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class InternalErrorException extends AsapException {
    public InternalErrorException(final Error error) {
        super(error);
    }
}
