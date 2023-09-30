package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
public class HostLoginStatusDto {
    HttpStatus httpStatus;
    HostLoginResponseDto hostLoginResponseDto;
}
