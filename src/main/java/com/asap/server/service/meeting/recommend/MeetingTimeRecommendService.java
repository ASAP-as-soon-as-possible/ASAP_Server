package com.asap.server.service.meeting.recommend;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.FindBestTimeBlockStrategy;
import com.asap.server.service.meeting.recommend.strategy.FindMeetingTimeCasesStrategy;
import com.asap.server.service.meeting.recommend.strategy.FindTimeBlockStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetingTimeRecommendService {
    private static final int BEST_MEETING_TIME_SIZE = 3;
    private final FindMeetingTimeCasesStrategy findMeetingTimeCasesStrategy;
    private final FindTimeBlockStrategy findTimeBlockStrategy;
    private final FindBestTimeBlockStrategy findBestTimeBlockStrategy;

    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlockDto> timeBlocks,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = findMeetingTimeCasesStrategy.find(duration, userCount);

        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (PossibleTimeCaseVo timeCase : timeCases) {
            List<TimeBlockDto> timeBlocksFilteredUserCount = timeBlocks.stream()
                    .filter(t -> t.userCount() == timeCase.memberCnt())
                    .toList();

            List<BestMeetingTimeVo> candidateMeetingTimes = new ArrayList<>(findTimeBlockStrategy.find(timeBlocksFilteredUserCount, timeCase.duration()));
            candidateMeetingTimes = findBestTimeBlockStrategy.find(candidateMeetingTimes);
            bestMeetingTimes.addAll(candidateMeetingTimes);

            if (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
                continue;
            }

            return bestMeetingTimes.stream().limit(BEST_MEETING_TIME_SIZE).collect(Collectors.toList());
        }

        while (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }
}
