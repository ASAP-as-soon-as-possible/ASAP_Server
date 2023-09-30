package com.asap.server.exception.model;

import com.asap.server.controller.dto.response.HostLoginResponseDto;
import com.asap.server.exception.Error;
import lombok.Getter;

@Getter
public class HostTimeForbiddenException extends AsapException {
    private final HostLoginResponseDto data;

    public HostTimeForbiddenException(Error error, HostLoginResponseDto data) {
        super(error);
        this.data = data;
    }
}
