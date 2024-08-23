package com.asap.server.service.time.vo;

import com.asap.server.persistence.domain.enums.TimeSlot;

import com.asap.server.service.meeting.dto.UserDto;
import java.time.LocalDate;
import java.util.List;

public record BestMeetingTimeWithUsers(
        LocalDate date,
        TimeSlot startTime,
        TimeSlot endTime,
        int weight,
        List<UserDto> users
) {
    public static BestMeetingTimeWithUsers of(
            final BestMeetingTimeVo bestMeetingTimeVo,
            final List<UserDto> users
    ) {
        return new BestMeetingTimeWithUsers(
                bestMeetingTimeVo.date(),
                bestMeetingTimeVo.startTime(),
                bestMeetingTimeVo.endTime(),
                bestMeetingTimeVo.weight(),
                users
        );
    }
}
