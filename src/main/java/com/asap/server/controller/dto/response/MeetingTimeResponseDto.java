package com.asap.server.controller.dto.response;

import com.asap.server.service.vo.AvailableMeetingTimeVo;
import com.asap.server.service.vo.UserVo;
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
    private List<UserVo> users;

    public static MeetingTimeResponseDto of(AvailableMeetingTimeVo availableMeetingTime) {
        if (availableMeetingTime == null) return null;
        String month = Integer.valueOf(availableMeetingTime.getDate().substring(0, 2)).toString();
        String day = Integer.valueOf(availableMeetingTime.getDate().substring(3, 5)).toString();
        String dayOfWeek = availableMeetingTime.getDate().substring(6, 7);
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
