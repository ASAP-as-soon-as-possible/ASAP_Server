package com.asap.server.common.exception.model;

import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import com.asap.server.common.exception.Error;
import lombok.Getter;

@Getter
public class HostTimeForbiddenException extends AsapException {
    private final String data;

    public HostTimeForbiddenException(Error error, String data) {
        super(error);
        this.data = data;
    }
}
