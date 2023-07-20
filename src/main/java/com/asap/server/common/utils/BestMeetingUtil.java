package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableMeetingTimeDto;
import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.PossibleTimeCaseDto;
import com.asap.server.controller.dto.response.TimeSlotInfoDto;
import com.asap.server.controller.dto.response.UserDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BestMeetingUtil {
    private Map<String, Map<TimeSlot, TimeSlotInfoDto>> timeTable;
    private MeetingDto meeting;
    private final TimeSlot[] timeSlots = TimeSlot.values();
    private final Duration[] durations = Duration.values();
    private Map<Duration, List<AvailableMeetingTimeDto>> availableMeetingTimesByDuration;
    private List<PossibleTimeCaseDto> timeCases;
    private List<AvailableMeetingTimeDto> fixedMeetingTime;

    public void getBestMeetingTime(MeetingDto meeting, List<MeetingTimeDto> meetingTimes) {
        timeTable = new HashMap<>();
        timeCases = new ArrayList<>();
        availableMeetingTimesByDuration = new HashMap<>();
        fixedMeetingTime = new ArrayList<>();
        this.meeting = meeting;
        initTimeTable();
        setUserMeetingTime(meetingTimes);
        getAllPossibleMeetingTimeCases(meeting.getDuration());
        collectAvailableMeetingTime();
        selectBestMeetingTime();
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
                timeSlotInfo.addUserName(meetingTime.getUser());
                timeSlotInfo.addWeight(meetingTime.getPriority());
            }
        }
    }

    private void collectAvailableMeetingTime() {
        for (Duration duration : durations) {
            collectAvailableMeetingTimeByDuration(duration);
            sortTimeTable(duration);
        }
    }

    private void collectAvailableMeetingTimeByDuration(Duration duration) {
        int needBlock = duration.getNeedBlock();
        Set<String> timeTableColumns = timeTable.keySet();
        for (String timeTableColumn : timeTableColumns) {
            Map<TimeSlot, TimeSlotInfoDto> timeSlotInfos = timeTable.get(timeTableColumn);

            for (int startTimeSlotIndex = 0; startTimeSlotIndex < timeSlots.length - needBlock + 1; startTimeSlotIndex++) {
                List<UserDto> userNames = timeSlotInfos.get(timeSlots[startTimeSlotIndex]).getUsers();

                if (!userNames.isEmpty()) {
                    List<UserDto> resultUserNames = new ArrayList<>();

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
            List<UserDto> users,
            List<UserDto> resultUsers,
            int startTimeSlotIndex,
            int needBlock
    ) {
        for (UserDto userName : users) {
            int count = 1;

            for (int block = startTimeSlotIndex + 1; block < startTimeSlotIndex + needBlock; block++) {
                List<UserDto> nextUserNames = timeSlotInfos.get(timeSlots[block]).getUsers();
                if (!nextUserNames.contains(userName)) break;
                count++;
            }

            if (count == needBlock) {
                resultUsers.add(userName);
            }
        }
    }

    private void addAvailableMeetingTimeByDuration(
            Map<TimeSlot, TimeSlotInfoDto> timeSlotInfos,
            List<UserDto> resultUserNames,
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
                resultUserNames,
                false
        );

        availableMeetingTimesByDuration.get(duration).add(result);
    }

    private void sortTimeTable(Duration duration) {
        List<AvailableMeetingTimeDto> availableMeetingTimes = availableMeetingTimesByDuration.get(duration);
        if (!availableMeetingTimes.isEmpty()) {
            Collections.sort(availableMeetingTimes);
        }
    }

    private void selectBestMeetingTime() {
        while (fixedMeetingTime.size() != 3) {
            int index;
            for (index = 0; index < timeCases.size(); index++) {
                AvailableMeetingTimeDto meetingTime = getBestMeetingTime(timeCases.get(index).getDuration(), timeCases.get(index).getMemberCnt());
                if (meetingTime != null) {
                    fixedMeetingTime.add(meetingTime);
                    break;
                }
            }
            if (index == timeCases.size()) {
                while (fixedMeetingTime.size() != 3) {
                    fixedMeetingTime.add(null);
                }
            }
        }
    }

    private AvailableMeetingTimeDto getBestMeetingTime(Duration duration, int memberCount) {
        List<AvailableMeetingTimeDto> availableMeetingTimes = availableMeetingTimesByDuration.get(duration);
        if (availableMeetingTimes.isEmpty()) {
            return null;
        }

        for (AvailableMeetingTimeDto availableMeetingTime : availableMeetingTimes) {
            if (availableMeetingTime.getUsers().size() == memberCount && !availableMeetingTime.isFixed()) {
                availableMeetingTime.setIsFixed();
                return availableMeetingTime;
            }
        }
        return null;
    }

    private void getAllPossibleMeetingTimeCases(Duration duration) {
        int memberCount = meeting.getUsers().size();
        while (memberCount > 0) {
            getPossibleMeetingTimeCases(duration, memberCount);
            memberCount = memberCount / 2;
        }
    }

    private void getPossibleMeetingTimeCases(Duration duration, int memberCount) {

        for (int count = memberCount; count > memberCount / 2; count--) {
            timeCases.add(new PossibleTimeCaseDto(durations[duration.ordinal()], count));
            if (duration.ordinal() > 0)
                timeCases.add(new PossibleTimeCaseDto(durations[duration.ordinal() - 1], count));
        }

        int secondDuration = (duration.ordinal() >= 2) ? duration.ordinal() - 2 : -1;

        for (int durationCount = secondDuration; durationCount > -1; durationCount--) {
            for (int count = memberCount; count > memberCount / 2; count--) {
                timeCases.add(new PossibleTimeCaseDto(durations[durationCount], count));
            }
        }
    }
}
