package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMeetingTimeResponseDto {
    private String url;
    private String accessToken;
}
