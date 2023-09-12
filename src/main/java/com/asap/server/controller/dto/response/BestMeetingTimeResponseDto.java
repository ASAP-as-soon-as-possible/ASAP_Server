package com.asap.server.controller.dto.response;

import com.asap.server.service.vo.BestMeetingTimeVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class BestMeetingTimeResponseDto {
    private int memberCount;
    private MeetingTimeResponseDto bestDateTime;
    private List<MeetingTimeResponseDto> otherDateTimes;

    private static final int FIRST_BEST_MEETING_INDEX = 0;
    private static final int SECOND_BEST_MEETING_INDEX = 1;
    private static final int THIRD_BEST_MEETING_INDEX = 2;

    public static BestMeetingTimeResponseDto of(int memberCount, List<BestMeetingTimeVo> bestMeetingTimes) {
        return new BestMeetingTimeResponseDto(
                memberCount,
                MeetingTimeResponseDto.of(bestMeetingTimes.get(FIRST_BEST_MEETING_INDEX)),
                Arrays.asList(
                        MeetingTimeResponseDto.of(bestMeetingTimes.get(SECOND_BEST_MEETING_INDEX)),
                        MeetingTimeResponseDto.of(bestMeetingTimes.get(THIRD_BEST_MEETING_INDEX))
                )
        );
    }
}
