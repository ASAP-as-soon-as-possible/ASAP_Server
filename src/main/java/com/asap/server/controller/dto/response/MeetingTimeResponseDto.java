package com.asap.server.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingTimeResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private List<UserDto> users;

    public static MeetingTimeResponseDto of(AvailableMeetingTimeDto availableMeetingTime) {
        if (availableMeetingTime == null) return null;
        String month = Integer.valueOf(availableMeetingTime.getDate().substring(0, 1)).toString();
        String day = Integer.valueOf(availableMeetingTime.getDate().substring(2, 4)).toString();
        String dayOfWeek = availableMeetingTime.getDate().substring(5, 6);
        return new MeetingTimeResponseDto(
                month,
                day,
                dayOfWeek,
                availableMeetingTime.getStartTime().getTime(),
                availableMeetingTime.getEndTime().getTime(),
                availableMeetingTime.getUsers()
        );
    }
}
