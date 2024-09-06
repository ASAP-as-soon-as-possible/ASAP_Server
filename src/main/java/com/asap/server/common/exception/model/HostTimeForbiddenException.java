package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;
import lombok.Getter;

@Getter
public class HostTimeForbiddenException extends AsapException {
    private final String data;

    public HostTimeForbiddenException(final Error error, final String data) {
        super(error);
        this.data = data;
    }
}
