package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTimeSaveRequestDto {

    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot startTime;

    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot endTime;
}
