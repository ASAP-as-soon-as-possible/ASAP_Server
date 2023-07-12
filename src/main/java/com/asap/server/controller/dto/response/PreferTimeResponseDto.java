package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreferTimeResponseDto {
    private String startTime;
    private String endTime;
}
