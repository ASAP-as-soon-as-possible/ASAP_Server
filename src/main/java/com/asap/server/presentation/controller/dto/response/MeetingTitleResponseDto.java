package com.asap.server.presentation.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class MeetingTitleResponseDto {
    private String title;
}
