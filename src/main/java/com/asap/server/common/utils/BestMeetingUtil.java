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
    private final TimeSlot[] timeSlots = TimeSlot.values();
    private final Duration[] durations = Duration.values();

    public void getBestMeetingTime(MeetingDto meeting, List<MeetingTimeDto> meetingTimes) {
        this.meeting = meeting;
        initTimeTable();
        setUserMeetingTime(meetingTimes);
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

    private void setUserMeetingTime(List<MeetingTimeDto> meetingTimes) {
        for (MeetingTimeDto meetingTime : meetingTimes) {
            String col = String.format("%s.%s.%s", meetingTime.getMonth(), meetingTime.getDay(), meetingTime.getDayOfWeek());
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(meetingTime.getStartTime().ordinal(), meetingTime.getEndTime().ordinal());
            Map<TimeSlot, TimeSlotInfoDto> rowTable = timeTable.get(col);

            for (TimeSlot timeSlot : timeSlots) {
                TimeSlotInfoDto timeSlotInfo = rowTable.get(timeSlot);
                timeSlotInfo.addUserName(meetingTime.getName());
                timeSlotInfo.addWeight(meetingTime.getPriority());
            }
        }
    }

}
