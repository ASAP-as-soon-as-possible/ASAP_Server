package com.asap.server.service.vo;

import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MeetingTimeVo {
    private String month;
    private String day;
    private String dayOfWeek;
    private TimeSlot startTime;
    private TimeSlot endTime;

    public static MeetingTimeVo of(MeetingTime meetingTime) {
        return new MeetingTimeVo(
                meetingTime.getMonth(),
                meetingTime.getDay(),
                meetingTime.getDayOfWeek(),
                meetingTime.getStartTime(),
                meetingTime.getEndTime());
    }
}
