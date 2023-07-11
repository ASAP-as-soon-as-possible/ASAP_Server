package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingSaveResponseDto {
    private String url;
    private String accessToken;
}