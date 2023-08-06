package com.asap.server.controller.dto.response;

import com.asap.server.service.vo.AvailableMeetingTimeVo;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BestMeetingTimeResponseDto {
    private int memberCount;
    private MeetingTimeResponseDto bestDateTime;
    private List<MeetingTimeResponseDto> otherDateTimes;

    public static BestMeetingTimeResponseDto of(int memberCount, List<AvailableMeetingTimeVo> availableMeetingTimes) {
        return new BestMeetingTimeResponseDto(
                memberCount,
                MeetingTimeResponseDto.of(availableMeetingTimes.get(0)),
                Arrays.asList(MeetingTimeResponseDto.of(availableMeetingTimes.get(1)), MeetingTimeResponseDto.of(availableMeetingTimes.get(2)))
        );
    }
}
