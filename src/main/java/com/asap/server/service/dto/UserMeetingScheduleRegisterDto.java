package com.asap.server.service.dto;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.presentation.controller.dto.request.UserMeetingTimeSaveRequestDto;

import java.util.List;

public record UserMeetingScheduleRegisterDto(
        String month,
        String day,
        TimeSlot startTime,
        TimeSlot endTime,
        int priority
) {

    public static List<UserMeetingScheduleRegisterDto> of(final List<UserMeetingTimeSaveRequestDto> targets) {
        return targets.stream().map(
                t -> new UserMeetingScheduleRegisterDto(
                        t.month(),
                        t.day(),
                        t.startTime(),
                        t.endTime(),
                        t.priority()
                )).toList();
    }
}
