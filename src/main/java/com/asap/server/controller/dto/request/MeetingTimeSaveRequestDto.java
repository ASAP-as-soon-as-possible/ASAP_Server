package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.*;

@Getter
@AllArgsConstructor
public class MeetingTimeSaveRequestDto {

    private DateDto date;
    private List<ScheduleDto> schedule;
    private class DateDto{
        private String month;
        private String day;
        private String dayOfWeek;
    }
    private class ScheduleDto{
        private TimeSlot startTime;
        private TimeSlot endTime;
        private int priority;
    }
}