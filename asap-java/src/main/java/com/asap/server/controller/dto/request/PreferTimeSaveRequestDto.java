package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;

public record PreferTimeSaveRequestDto(
        @Schema(description = "선호 시간(시작)", example = "09:00")
        TimeSlot startTime,

        @Schema(description = "선호 시간(끝)", example = "11:00")
        TimeSlot endTime
) {
}
