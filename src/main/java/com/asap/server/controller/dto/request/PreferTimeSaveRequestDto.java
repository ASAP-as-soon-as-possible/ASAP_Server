package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PreferTimeSaveRequestDto {

    private TimeSlot startTime;

    private TimeSlot endTime;
}
