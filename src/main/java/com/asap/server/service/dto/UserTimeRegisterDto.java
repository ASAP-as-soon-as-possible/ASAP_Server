package com.asap.server.service.dto;

import com.asap.server.presentation.controller.dto.request.AvailableTimeRequestDto;

import java.util.List;

public record UserTimeRegisterDto(
        String name,
        List<UserMeetingScheduleRegisterDto> availableSchedules
) {
    public static UserTimeRegisterDto of(final AvailableTimeRequestDto target) {
        return new UserTimeRegisterDto(target.getName(), UserMeetingScheduleRegisterDto.of(target.getAvailableTimes()));
    }
}
