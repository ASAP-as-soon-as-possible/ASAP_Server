package com.asap.server.service.vo;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.enums.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingVo {
    List<DateAvailabilityVo> availabilities;
    Duration duration;
    List<UserVo> users;

    public static MeetingVo of(Meeting meeting) {
        List<DateAvailabilityVo> availabilities = meeting.getDateAvailabilities()
                .stream()
                .map(DateAvailabilityVo::of)
                .collect(Collectors.toList());
        List<UserVo> users = meeting.getUsers()
                .stream()
                .map(UserVo::of)
                .collect(Collectors.toList());
        return new MeetingVo(availabilities, meeting.getDuration(), users);
    }
}
