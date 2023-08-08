package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTimeSaveRequestDto {

    @Schema(description = "선호 시간(시작)", example = "09:00")
    private TimeSlot startTime;

    @Schema(description = "선호 시간(끝)", example = "11:00")
    private TimeSlot endTime;
}
