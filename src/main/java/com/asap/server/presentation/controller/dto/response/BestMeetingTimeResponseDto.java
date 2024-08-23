package com.asap.server.presentation.controller.dto.response;

import com.asap.server.service.meeting.dto.BestMeetingTimeDto;
import java.util.List;

public record BestMeetingTimeResponseDto(
        int memberCount,
        MeetingTimeResponseDto bestDateTime,
        List<MeetingTimeResponseDto> otherDateTimes
) {
    public static BestMeetingTimeResponseDto of(final BestMeetingTimeDto bestMeetingTimeDto) {
        return new BestMeetingTimeResponseDto(
                bestMeetingTimeDto.memberCount(),
                bestMeetingTimeDto.bestDateTime(),
                bestMeetingTimeDto.otherDateTimes()
        );
    }
}
