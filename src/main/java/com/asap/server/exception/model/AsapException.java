package com.asap.server.exception.model;

import com.asap.server.exception.Error;
import lombok.Getter;

@Getter
public class AsapException extends RuntimeException {
    private final Error error;

    public AsapException(Error error, String message){
        super(error.getMessage());
        this.error = error;
    }
    public int getHttpStatus(){
        return error.getHttpStatusCode();
    }
}
