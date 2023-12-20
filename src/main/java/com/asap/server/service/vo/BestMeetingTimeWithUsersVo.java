package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public record BestMeetingTimeWithUsersVo(
        LocalDate date,
        TimeSlot startTime,
        TimeSlot endTime,
        int weight,
        List<UserVo> users
) {
    public static BestMeetingTimeWithUsersVo of(
            final BestMeetingTimeVo bestMeetingTimeVo,
            final List<UserVo> users
    ) {
        return new BestMeetingTimeWithUsersVo(
                bestMeetingTimeVo.date(),
                bestMeetingTimeVo.startTime(),
                bestMeetingTimeVo.endTime(),
                bestMeetingTimeVo.weight(),
                users
        );
    }
}
