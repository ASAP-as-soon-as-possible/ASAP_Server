package com.asap.server.controller.dto.response;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.enums.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingDto {
    List<DateAvailabilityDto> availabilities;
    Duration duration;
    List<UserDto> users;

    public static MeetingDto of(Meeting meeting) {
        List<DateAvailabilityDto> availabilities = meeting.getDateAvailabilities()
                .stream()
                .map(DateAvailabilityDto::of)
                .collect(Collectors.toList());
        List<UserDto> users = meeting.getUsers()
                .stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
        return new MeetingDto(availabilities, meeting.getDuration(), users);
    }
}
