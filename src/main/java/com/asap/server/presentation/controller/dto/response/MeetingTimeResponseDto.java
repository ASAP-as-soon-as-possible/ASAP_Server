package com.asap.server.presentation.controller.dto.response;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.service.meeting.dto.UserDto;
import com.asap.server.service.time.vo.BestMeetingTimeWithUsers;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class MeetingTimeResponseDto {
    private String month;
    private String day;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private List<UserDto> users;

    public static MeetingTimeResponseDto of(BestMeetingTimeWithUsers bestMeetingTime) {
        if (bestMeetingTime == null) return null;
        return new MeetingTimeResponseDto(
                DateUtil.getMonth(bestMeetingTime.date()),
                DateUtil.getDay(bestMeetingTime.date()),
                DateUtil.getDayOfWeek(bestMeetingTime.date()),
                bestMeetingTime.startTime().getTime(),
                bestMeetingTime.endTime().getTime(),
                bestMeetingTime.users()
        );
    }
}
