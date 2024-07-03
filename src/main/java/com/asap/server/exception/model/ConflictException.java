package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class ConflictException extends AsapException{

    public ConflictException(Error error){
        super(error);
    }
}
