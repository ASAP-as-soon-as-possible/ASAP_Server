package com.asap.server.service.meeting;

import com.asap.server.domain.enums.Duration;
import com.asap.server.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeetingTimeRecommendService {
    private static final int BEST_MEETING_TIME_SIZE = 3;
    private final FindBestMeetingTimeCasesStrategy findBestMeetingTimeCasesStrategy;
    private final FindBestMeetingTimeStrategy findBestMeetingTimeStrategy;

    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlockDto> timeBlocks,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = findBestMeetingTimeCasesStrategy.find(duration, userCount);
        List<BestMeetingTimeVo> bestMeetingTimes = findAllBestMeetingTimes(timeBlocks, timeCases);

        while (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }

    public List<BestMeetingTimeVo> findAllBestMeetingTimes(
            final List<TimeBlockDto> timeBlocks,
            final List<PossibleTimeCaseVo> timeCases
    ) {
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (PossibleTimeCaseVo timeCase : timeCases) {
            List<BestMeetingTimeVo> bestMeetingTimesWithTimeCase = findBestMeetingTimeStrategy.find(timeBlocks, timeCase);

            bestMeetingTimes.addAll(bestMeetingTimesWithTimeCase);

            if (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) continue;

            return bestMeetingTimes
                    .stream()
                    .limit(BEST_MEETING_TIME_SIZE)
                    .collect(Collectors.toList());
        }
        return bestMeetingTimes;
    }
}
