package com.asap.server.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BestMeetingTimeResponseDto {
    private int memberCount;
    private MeetingTimeResponseDto bestDateTime;
    private List<MeetingTimeResponseDto> otherDateTimes;
}
