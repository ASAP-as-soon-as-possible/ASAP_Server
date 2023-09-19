package com.asap.server.controller.dto.response;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.service.vo.AvailableMeetingTimeVo;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
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

    public static MeetingTimeResponseDto of(BestMeetingTimeVo bestMeetingTime) {
        if (bestMeetingTime == null) return null;
        return new MeetingTimeResponseDto(
                DateUtil.getMonth(bestMeetingTime.getDate()),
                DateUtil.getDay(bestMeetingTime.getDate()),
                DateUtil.getDayOfWeek(bestMeetingTime.getDate()),
                bestMeetingTime.getStartTime().getTime(),
                bestMeetingTime.getEndTime().getTime(),
                bestMeetingTime.getUsers()
        );
    }
}
