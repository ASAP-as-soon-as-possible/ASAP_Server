package com.asap.server.controller.dto.response;

import com.asap.server.domain.PreferTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PreferTimeResponseDto {
    private String startTime;
    private String endTime;

    public static PreferTimeResponseDto of(PreferTime preferTime) {
        return new PreferTimeResponseDto(preferTime.getStartTime().getTime(), preferTime.getEndTime().getTime());
    }
}
