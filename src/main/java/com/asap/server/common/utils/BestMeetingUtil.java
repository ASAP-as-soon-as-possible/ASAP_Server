package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableMeetingTimeDto;
import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.TimeSlotInfoDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BestMeetingUtil {
    private final Map<String, Map<TimeSlot, TimeSlotInfoDto>> timeTable = new HashMap<>();
    private MeetingDto meeting;
    private final TimeSlot[] timeSlots = TimeSlot.values();
    private final Duration[] durations = Duration.values();
    private final Map<Duration, List<AvailableMeetingTimeDto>> availableMeetingTimesByDuration = new HashMap<>();

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

        for (Duration d : durations) {
            availableMeetingTimesByDuration.put(d, new ArrayList<>());
        }
    }

    private void setUserMeetingTime(List<MeetingTimeDto> meetingTimes) {
        for (MeetingTimeDto meetingTime : meetingTimes) {
            String availableDateColumn = String.format("%s.%s.%s", meetingTime.getMonth(), meetingTime.getDay(), meetingTime.getDayOfWeek());
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(meetingTime.getStartTime().ordinal(), meetingTime.getEndTime().ordinal());
            Map<TimeSlot, TimeSlotInfoDto> rowTable = timeTable.get(availableDateColumn);

            for (TimeSlot timeSlot : timeSlots) {
                TimeSlotInfoDto timeSlotInfo = rowTable.get(timeSlot);
                timeSlotInfo.addUserName(meetingTime.getName());
                timeSlotInfo.addWeight(meetingTime.getPriority());
            }
        }
    }

    private void collectAvailableMeetingTimeByDuration(Duration duration) {
        int needBlock = duration.getNeedBlock();
        Set<String> timeTableColumns = timeTable.keySet();
        for (String timeTableColumn : timeTableColumns) {
            Map<TimeSlot, TimeSlotInfoDto> timeSlotInfos = timeTable.get(timeTableColumn);

            for (int startTimeSlotIndex = 0; startTimeSlotIndex < timeSlots.length - needBlock + 1; startTimeSlotIndex++) {
                List<String> userNames = timeSlotInfos.get(timeSlots[startTimeSlotIndex]).getUserNames();

                if (!userNames.isEmpty()) {
                    List<String> resultUserNames = new ArrayList<>();

                    checkAvailableMeetingTimeByDuration(
                            timeSlotInfos,
                            userNames,
                            resultUserNames,
                            startTimeSlotIndex,
                            needBlock
                    );

                    if (!resultUserNames.isEmpty()) {
                        addAvailableMeetingTimeByDuration(
                                timeSlotInfos,
                                resultUserNames,
                                duration,
                                timeTableColumn,
                                startTimeSlotIndex
                        );
                    }
                }
            }
        }
    }

    private void checkAvailableMeetingTimeByDuration(
            Map<TimeSlot, TimeSlotInfoDto> timeSlotInfos,
            List<String> userNames,
            List<String> resultUserNames,
            int startTimeSlotIndex,
            int needBlock
    ) {
        for (String userName : userNames) {
            int count = 1;

            for (int block = startTimeSlotIndex + 1; block < startTimeSlotIndex + needBlock; block++) {
                List<String> nextUserNames = timeSlotInfos.get(timeSlots[block]).getUserNames();
                if (!nextUserNames.contains(userName)) break;
                count++;
            }

            if (count == needBlock) {
                resultUserNames.add(userName);
            }
        }
    }

    private void addAvailableMeetingTimeByDuration(
            Map<TimeSlot, TimeSlotInfoDto> timeSlotInfos,
            List<String> resultUserNames,
            Duration duration,
            String timeTableColumn,
            int startTimeSlotIndex
    ) {
        int needBlock = duration.getNeedBlock();
        TimeSlot startTime = timeSlots[startTimeSlotIndex];
        TimeSlot endTime = timeSlots[startTimeSlotIndex + needBlock - 1];

        int weight = 0;
        for (int block = startTimeSlotIndex + 1; block < startTimeSlotIndex + needBlock; block++) {
            weight += timeSlotInfos.get(timeSlots[block]).getWeight();
        }

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto(
                timeTableColumn,
                startTime,
                endTime,
                weight / (needBlock - 1),
                resultUserNames
        );

        availableMeetingTimesByDuration.get(duration).add(result);
    }
}
