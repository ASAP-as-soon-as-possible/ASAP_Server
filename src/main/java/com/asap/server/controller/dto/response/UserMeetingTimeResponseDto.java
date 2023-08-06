package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserMeetingTimeResponseDto {
    private String url;
    private String accessToken;
}
