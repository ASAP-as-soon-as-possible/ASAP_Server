package com.asap.server.service.time.dto.register;

import java.util.List;

public record UserTimeRegisterDto(
        String name,
        List<UserMeetingScheduleRegisterDto> availableSchedules
) {
}
