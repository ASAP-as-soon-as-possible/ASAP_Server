package com.asap.server.controller.dto.response;

import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MeetingTimeDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private UserDto user;
    private int priority;

    public static MeetingTimeDto of(MeetingTime meetingTime) {
        return new MeetingTimeDto(
                meetingTime.getMonth(),
                meetingTime.getDay(),
                meetingTime.getDayOfWeek(),
                meetingTime.getStartTime(),
                meetingTime.getEndTime(),
                UserDto.of(meetingTime.getUser()),
                meetingTime.getPriority()
        );
    }
}
