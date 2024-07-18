package com.asap.server.common.exception.model;

import com.asap.server.common.exception.Error;

public class ConflictException extends AsapException{

    public ConflictException(Error error){
        super(error);
    }
}
