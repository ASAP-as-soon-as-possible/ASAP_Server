package com.asap.server.exception.model;

import com.asap.server.exception.Error;
import lombok.Getter;

@Getter
public class AsapException extends RuntimeException {
    private Error error;

    public AsapException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public AsapException(String msg) {
        super(msg);
    }

    public int getHttpStatus() {
        return error.getHttpStatusCode();
    }
}
