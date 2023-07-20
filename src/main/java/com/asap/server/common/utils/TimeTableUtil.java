package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableDatesDto;
import com.asap.server.controller.dto.response.TimeSlotDto;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.MeetingTimeVo;
import com.asap.server.service.vo.UserVo;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class TimeTableUtil {
    private final List<UserVo> users = new ArrayList<>();
    private final List<String> userNames = new ArrayList<>();
    private final Map<String, Map<String, List<String>>> dateAvailable = new HashMap<>();
    private final List<AvailableDatesDto> availableDatesDtoList = new ArrayList<>();
    public void setTimeTable(UserVo user, List<MeetingTimeVo> meetingTimes) {
        for (MeetingTimeVo meetingTime : meetingTimes) {
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(meetingTime.getStartTime().ordinal(), meetingTime.getEndTime().ordinal());
            for (TimeSlot timeSlot : timeSlots) {
                String colTime = timeSlot.getTime();
                String col = String.format("%s %s %s", meetingTime.getMonth(), meetingTime.getDay(), meetingTime.getDayOfWeek());
                if (dateAvailable.containsKey(col)) {
                    if (dateAvailable.get(col).containsKey(colTime)) {
                        dateAvailable.get(col).get(colTime).add(user.getName());
                    } else {
                        List<String> name = new ArrayList<>();
                        name.add(user.getName());
                        dateAvailable.get(col).put(colTime, name);
                    }
                } else {
                    Map<String, List<String>> timeAvailable = new HashMap<>();
                    List<String> name = new ArrayList<>();
                    name.add(user.getName());
                    timeAvailable.put(colTime, name);
                    dateAvailable.put(col, timeAvailable);
                }
            }
        }
        users.add(user);
        userNames.add(user.getName());
        setColorLevel();
    }
    private void setColorLevel(){
        dateAvailable.forEach((key, value) -> {
                    List<TimeSlotDto> timeSlotDtoList = new ArrayList<>();
                    value.forEach((timeSlot, userNameList) ->
                            {
                                int colorLevel = getColorLevel(userNameList);
                                timeSlotDtoList.add(TimeSlotDto
                                        .builder()
                                        .time(timeSlot)
                                        .userNames(userNameList)
                                        .colorLevel(colorLevel)
                                        .build());
                            }
                    );
                    Collections.sort(timeSlotDtoList, Comparator.comparing(TimeSlotDto::getTime));
                    String month = key.substring(0, 2);
                    String day = key.substring(3, 5);
                    String dayOfWeek = key.substring(6, 7);

                    availableDatesDtoList.add(AvailableDatesDto
                            .builder()
                            .month(Integer.valueOf(month).toString())
                            .day(Integer.valueOf(day).toString())
                            .dayOfWeek(dayOfWeek)
                            .timeSlots(timeSlotDtoList)
                            .build()
                    );
                }
        );

    }
    private int getColorLevel(List<String> userNameList){
        double ratio = (double) userNameList.size() / users.size();

        if (ratio <= 0.2) {
            return 1;
        } else if (ratio <= 0.4) {
            return 2;
        } else if (ratio <= 0.6) {
            return 3;
        } else if (ratio <= 0.8) {
            return 4;
        } else if (ratio <= 1.0) {
            return 5;
        } else {
            return 0;
        }
    }
}

