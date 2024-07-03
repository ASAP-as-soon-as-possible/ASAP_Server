package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class PreferTimeResponseDto {
    private String startTime;
    private String endTime;
}
