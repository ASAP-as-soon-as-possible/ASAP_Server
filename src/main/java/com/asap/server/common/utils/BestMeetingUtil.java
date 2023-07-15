package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.TimeSlotInfoDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BestMeetingUtil {
    private final Map<String, Map<TimeSlot, TimeSlotInfoDto>> timeTable = new HashMap<>();
    private MeetingDto meeting;
    private List<MeetingTimeDto> meetingTimes;
    private final TimeSlot[] timeSlots = TimeSlot.values();
    private final Duration[] durations = Duration.values();

    public void getBestMeetingTime(MeetingDto meeting, List<MeetingTimeDto> meetingTimes) {
        this.meeting = meeting;
        this.meetingTimes = meetingTimes;
        initTimeTable();
    }

    private void initTimeTable() {
        for (DateAvailabilityDto dateAvailability : meeting.getAvailabilities()) {
            Map<TimeSlot, TimeSlotInfoDto> rowTable = new HashMap<>();
            for (TimeSlot timeSlot : timeSlots) {
                rowTable.put(timeSlot, new TimeSlotInfoDto());
            }
            String col = String.format("%s.%s.%s", dateAvailability.getMonth(), dateAvailability.getDay(), dateAvailability.getDayOfWeek());
            timeTable.put(col, rowTable);
        }
    }
}
