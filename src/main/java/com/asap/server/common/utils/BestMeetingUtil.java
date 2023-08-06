package com.asap.server.common.utils;

import com.asap.server.service.vo.AvailableMeetingTimeVo;
import com.asap.server.service.vo.DateAvailabilityVo;
import com.asap.server.service.vo.MeetingTimeVo;
import com.asap.server.service.vo.MeetingVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import com.asap.server.service.vo.TimeSlotInfoDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.UserVo;
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
    private MeetingVo meeting;
    private final TimeSlot[] timeSlots = TimeSlot.values();
    private final Duration[] durations = Duration.values();
    private Map<Duration, List<AvailableMeetingTimeVo>> availableMeetingTimesByDuration;
    private List<PossibleTimeCaseVo> timeCases;
    private List<AvailableMeetingTimeVo> fixedMeetingTime;

    public void getBestMeetingTime(MeetingVo meeting, List<MeetingTimeVo> meetingTimes) {
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
        for (DateAvailabilityVo dateAvailability : meeting.getAvailabilities()) {
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

    private void setUserMeetingTime(List<MeetingTimeVo> meetingTimes) {
        for (MeetingTimeVo meetingTime : meetingTimes) {
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
                List<UserVo> userNames = timeSlotInfos.get(timeSlots[startTimeSlotIndex]).getUsers();

                if (!userNames.isEmpty()) {
                    List<UserVo> resultUserNames = new ArrayList<>();

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
            List<UserVo> users,
            List<UserVo> resultUsers,
            int startTimeSlotIndex,
            int needBlock
    ) {
        for (UserVo userName : users) {
            int count = 1;

            for (int block = startTimeSlotIndex + 1; block < startTimeSlotIndex + needBlock; block++) {
                List<UserVo> nextUserNames = timeSlotInfos.get(timeSlots[block]).getUsers();
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
            List<UserVo> resultUserNames,
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

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo(
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
        List<AvailableMeetingTimeVo> availableMeetingTimes = availableMeetingTimesByDuration.get(duration);
        if (!availableMeetingTimes.isEmpty()) {
            Collections.sort(availableMeetingTimes);
        }
    }

    private void selectBestMeetingTime() {
        while (fixedMeetingTime.size() != 3) {
            int index;
            for (index = 0; index < timeCases.size(); index++) {
                AvailableMeetingTimeVo meetingTime = getBestMeetingTime(timeCases.get(index).getDuration(), timeCases.get(index).getMemberCnt());
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

    private AvailableMeetingTimeVo getBestMeetingTime(Duration duration, int memberCount) {
        List<AvailableMeetingTimeVo> availableMeetingTimes = availableMeetingTimesByDuration.get(duration);
        if (availableMeetingTimes.isEmpty()) {
            return null;
        }

        for (AvailableMeetingTimeVo availableMeetingTime : availableMeetingTimes) {
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
            timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal()], count));
            if (duration.ordinal() > 0)
                timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal() - 1], count));
        }

        int secondDuration = (duration.ordinal() >= 2) ? duration.ordinal() - 2 : -1;

        for (int durationCount = secondDuration; durationCount > -1; durationCount--) {
            for (int count = memberCount; count > memberCount / 2; count--) {
                timeCases.add(new PossibleTimeCaseVo(durations[durationCount], count));
            }
        }
    }
}
