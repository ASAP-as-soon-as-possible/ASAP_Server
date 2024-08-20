package com.asap.server.service.dto;

import java.util.List;

public record UserTimeRegisterDto(
        String name,
        List<UserMeetingScheduleRegisterDto> availableSchedules
) {
}
