package com.asap.server.service.meeting.dto;

import com.asap.server.presentation.controller.dto.response.MeetingTimeResponseDto;
import com.asap.server.service.vo.BestMeetingTimeWithUsersVo;
import java.util.Arrays;
import java.util.List;

public record BestMeetingTimeDto(
        int memberCount,
        MeetingTimeResponseDto bestDateTime,
        List<MeetingTimeResponseDto> otherDateTimes
) {
    private static final int FIRST_BEST_MEETING_INDEX = 0;
    private static final int SECOND_BEST_MEETING_INDEX = 1;
    private static final int THIRD_BEST_MEETING_INDEX = 2;

    public static BestMeetingTimeDto of(
            final int memberCount,
            final List<BestMeetingTimeWithUsersVo> bestMeetingTimes
    ) {
        return new BestMeetingTimeDto(
                memberCount,
                MeetingTimeResponseDto.of(bestMeetingTimes.get(FIRST_BEST_MEETING_INDEX)),
                Arrays.asList(
                        MeetingTimeResponseDto.of(bestMeetingTimes.get(SECOND_BEST_MEETING_INDEX)),
                        MeetingTimeResponseDto.of(bestMeetingTimes.get(THIRD_BEST_MEETING_INDEX))
                )
        );
    }
}
