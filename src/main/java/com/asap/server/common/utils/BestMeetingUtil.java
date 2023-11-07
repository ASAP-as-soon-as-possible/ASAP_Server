package com.asap.server.common.utils;

import com.asap.server.common.utils.strategy.FindOptimalMeetingTimeCasesStrategy;
import com.asap.server.common.utils.strategy.FindOptimalMeetingTimeStrategy;
import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@RequiredArgsConstructor
public class BestMeetingUtil {
    private final FindOptimalMeetingTimeCasesStrategy findOptimalMeetingTimeCasesStrategy;
    private final FindOptimalMeetingTimeStrategy findOptimalMeetingTimeStrategy;

    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlocksByDateVo> timeBlocksByDates,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = findOptimalMeetingTimeCasesStrategy.findOptimalMeetingTimeCases(duration, userCount);
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();

        for (PossibleTimeCaseVo timeCase : timeCases) {
            timeBlocksByDates.forEach(timeBlocksByDate -> {
                List<BestMeetingTimeVo> bestMeetingTimeVos = findOptimalMeetingTimeStrategy.findOptimalMeetingTime(
                        timeBlocksByDate,
                        timeCase.getDuration().getNeedBlock(),
                        timeCase.getMemberCnt()
                );
                
                bestMeetingTimes.addAll(bestMeetingTimeVos);
            });
            if (bestMeetingTimes.size() > 2) return bestMeetingTimes
                    .stream()
                    .sorted(Comparator.comparing(BestMeetingTimeVo::getWeight, Comparator.reverseOrder()))
                    .limit(3)
                    .collect(Collectors.toList());
        }

        while (bestMeetingTimes.size() < 3) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }
}
