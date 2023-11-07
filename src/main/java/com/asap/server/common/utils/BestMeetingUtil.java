package com.asap.server.common.utils;

import com.asap.server.common.utils.strategy.FindOptimalMeetingTimeCasesStrategy;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import com.asap.server.service.vo.TimeBlockVo;
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

    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlocksByDateVo> timeBlocksByDates,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = findOptimalMeetingTimeCasesStrategy.findOptimalMeetingTimeCases(duration, userCount);
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();

        for (PossibleTimeCaseVo timeCase : timeCases) {
            timeBlocksByDates.forEach(timeBlocksByDate -> {
                bestMeetingTimes.addAll(
                        searchBestMeetingTime(timeBlocksByDate, timeCase.getDuration().getNeedBlock(), timeCase.getMemberCnt())
                );
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

    public List<BestMeetingTimeVo> searchBestMeetingTime(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount) {
        List<TimeBlockVo> sortedTimeBlocks = filterByUserCountAndSortByTime(timeBlocksByDate.getTimeBlocks(), userCount);

        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        int endIndex = sortedTimeBlocks.size() - needTimeBlockCount + 1;
        for (int timeBlockIdx = 0; timeBlockIdx < endIndex; timeBlockIdx++) {
            if (!isBestMeetingTime(sortedTimeBlocks, timeBlockIdx, needTimeBlockCount)) continue;

            int sumWeight = sortedTimeBlocks
                    .subList(timeBlockIdx, timeBlockIdx + needTimeBlockCount)
                    .stream()
                    .map(TimeBlockVo::getWeight)
                    .reduce(0, Integer::sum);

            TimeSlot startTime = sortedTimeBlocks.get(timeBlockIdx).getTimeSlot();
            BestMeetingTimeVo bestMeetingTime = new BestMeetingTimeVo(
                    timeBlocksByDate.getDate(),
                    startTime,
                    TimeSlot.getTimeSlot(startTime.ordinal() + needTimeBlockCount),
                    sortedTimeBlocks.get(timeBlockIdx).getUsers(),
                    sumWeight
            );
            bestMeetingTimes.add(bestMeetingTime);
        }

        return bestMeetingTimes;
    }

    private List<TimeBlockVo> filterByUserCountAndSortByTime(final List<TimeBlockVo> timeBlocks, final int userCount) {
        return timeBlocks.stream()
                .filter(timeBlockVo -> timeBlockVo.getUsers().size() == userCount)
                .sorted(TimeBlockVo::compareTo)
                .collect(Collectors.toList());
    }

    private boolean isBestMeetingTime(final List<TimeBlockVo> timeBlocks, final int timeBlockIdx, final int needTimeBlockCount) {
        boolean isBestMeetingTime = true;
        TimeSlot nextTime = timeBlocks.get(timeBlockIdx).getTimeSlot();
        for (int i = timeBlockIdx + 1; i < timeBlockIdx + needTimeBlockCount; i++) {
            if (nextTime.ordinal() + 1 != timeBlocks.get(i).getTimeSlot().ordinal()) {
                isBestMeetingTime = false;
                break;
            }
            nextTime = timeBlocks.get(i).getTimeSlot();
        }
        return isBestMeetingTime;
    }

}
