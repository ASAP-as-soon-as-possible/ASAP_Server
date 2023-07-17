package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTimeSaveRequestDto {

    private TimeSlot startTime;

    private TimeSlot endTime;
}
