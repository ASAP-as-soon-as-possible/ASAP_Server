package com.asap.server.common.utils;

import com.asap.server.common.utils.strategy.FindBestMeetingTimeCasesStrategy;
import com.asap.server.common.utils.strategy.FindBestMeetingTimeStrategy;
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
    private static final int BEST_MEETING_TIME_SIZE = 3;
    private final FindBestMeetingTimeCasesStrategy findBestMeetingTimeCasesStrategy;
    private final FindBestMeetingTimeStrategy findBestMeetingTimeStrategy;

    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlocksByDateVo> timeBlocksByDates,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = findBestMeetingTimeCasesStrategy.find(duration, userCount);
        List<BestMeetingTimeVo> bestMeetingTimes = findAllBestMeetingTimes(timeBlocksByDates, timeCases);

        while (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }

    private List<BestMeetingTimeVo> findAllBestMeetingTimes(final List<TimeBlocksByDateVo> timeBlocksByDates, final List<PossibleTimeCaseVo> timeCases) {
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (PossibleTimeCaseVo timeCase : timeCases) {
            List<BestMeetingTimeVo> bestMeetingTimesWithTimeCase = findBestMeetingTimesWithTimeCase(timeCase, timeBlocksByDates);
            bestMeetingTimes.addAll(bestMeetingTimesWithTimeCase);

            if (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) continue;

            return findTop3BestMeetingTimesSortByWeight(bestMeetingTimes);
        }
        return bestMeetingTimes;
    }

    private List<BestMeetingTimeVo> findBestMeetingTimesWithTimeCase(final PossibleTimeCaseVo timeCase, final List<TimeBlocksByDateVo> timeBlocksByDates) {
        int needBlock = timeCase.getDuration().getNeedBlock();
        int memberCount = timeCase.getMemberCnt();

        return timeBlocksByDates.stream()
                .map(timeBlocksByDateVo -> findBestMeetingTimeStrategy.find(timeBlocksByDateVo, needBlock, memberCount))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<BestMeetingTimeVo> findTop3BestMeetingTimesSortByWeight(final List<BestMeetingTimeVo> bestMeetingTimes) {
        return bestMeetingTimes.stream()
                .sorted(Comparator.comparing(BestMeetingTimeVo::weight, Comparator.reverseOrder()))
                .limit(BEST_MEETING_TIME_SIZE)
                .collect(Collectors.toList());
    }
}
