package com.asap.server.exception.model;

import com.asap.server.exception.Error;

public class UserMismatchException extends AsapException{
    public UserMismatchException(Error error){
        super(error);
    }
}
