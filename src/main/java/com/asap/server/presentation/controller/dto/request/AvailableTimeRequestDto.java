package com.asap.server.presentation.controller.dto.request;

import com.asap.server.service.dto.UserTimeRegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

@Schema(description = "사용자 가능 시간 DTO")
public record AvailableTimeRequestDto(
        @Schema(description = "사용자 이름", example = "김아삽")
        String name,
        @Schema(description = "가능 일자")
        List<@Valid UserMeetingTimeSaveRequestDto> availableTimes
) {
    public static UserTimeRegisterDto to(final AvailableTimeRequestDto target) {
        return new UserTimeRegisterDto(target.name(), UserMeetingTimeSaveRequestDto.to(target.availableTimes()));
    }
}
