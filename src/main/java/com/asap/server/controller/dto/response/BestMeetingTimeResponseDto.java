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

    public static BestMeetingTimeResponseDto of(int memberCount, List<BestMeetingTimeVo> bestMeetingTimes) {
        return new BestMeetingTimeResponseDto(
                memberCount,
                MeetingTimeResponseDto.of(bestMeetingTimes.get(0)),
                Arrays.asList(MeetingTimeResponseDto.of(bestMeetingTimes.get(1)), MeetingTimeResponseDto.of(bestMeetingTimes.get(2)))
        );
    }
}
