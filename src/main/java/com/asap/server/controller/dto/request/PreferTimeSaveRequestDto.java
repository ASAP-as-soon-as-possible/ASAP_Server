package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTimeSaveRequestDto {

    @Schema(description = "선호 시간(시작)", example = "09:00")
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot startTime;

    @Schema(description = "선호 시간(끝)", example = "11:00")
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot endTime;
}
