package com.asap.server.common.utils.strategy.impl;

import com.asap.server.common.utils.strategy.FindBestMeetingTimeStrategy;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FindBestMeetingTimeStrategyImpl implements FindBestMeetingTimeStrategy {
    @Override
    public List<BestMeetingTimeVo> find(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount) {
        List<TimeBlockVo> sortedTimeBlocks = filterByUserCountAndSortByTime(timeBlocksByDate.getTimeBlocks(), userCount);
        return findBestMeetingTime(sortedTimeBlocks, timeBlocksByDate.getDate(), needTimeBlockCount);
    }

    private List<BestMeetingTimeVo> findBestMeetingTime(final List<TimeBlockVo> timeBlocks, final LocalDate date, final int needTimeBlockCount) {
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();

        int endIndex = timeBlocks.size() - needTimeBlockCount + 1;
        for (int timeBlockIdx = 0; timeBlockIdx < endIndex; timeBlockIdx++) {
            int endIdx = timeBlockIdx + needTimeBlockCount;
            if (!isBestMeetingTime(timeBlocks, timeBlockIdx, endIdx)) continue;

            int sumWeight = sumTimeBlocksWeight(timeBlocks, timeBlockIdx, endIdx);

            BestMeetingTimeVo bestMeetingTime = getBestMeetingTime(
                    timeBlocks,
                    date,
                    timeBlockIdx,
                    needTimeBlockCount,
                    sumWeight
            );

            bestMeetingTimes.add(bestMeetingTime);
        }
        return bestMeetingTimes;
    }

    private List<TimeBlockVo> filterByUserCountAndSortByTime(final List<TimeBlockVo> timeBlocks, final int userCount) {
        return timeBlocks.stream()
                .filter(timeBlockVo -> timeBlockVo.userCount() == userCount)
                .sorted(TimeBlockVo::compareTo)
                .collect(Collectors.toList());
    }

    private boolean isBestMeetingTime(final List<TimeBlockVo> timeBlocks, final int timeBlockIdx, final int endIdx) {
        boolean isBestMeetingTime = true;
        TimeSlot nextTime = timeBlocks.get(timeBlockIdx).timeSlot();
        for (int i = timeBlockIdx + 1; i < endIdx; i++) {
            if (nextTime.ordinal() + 1 != timeBlocks.get(i).timeSlot().ordinal()) return false;

            nextTime = timeBlocks.get(i).timeSlot();
        }
        return isBestMeetingTime;
    }

    private int sumTimeBlocksWeight(final List<TimeBlockVo> timeBlocks, final int startIdx, final int endIdx) {
        return timeBlocks
                .subList(startIdx, endIdx)
                .stream()
                .map(TimeBlockVo::weight)
                .reduce(0, Integer::sum);
    }

    private BestMeetingTimeVo getBestMeetingTime(
            final List<TimeBlockVo> timeBlocks,
            final LocalDate date,
            final int timeBlockIdx,
            final int needTimeBlockCount,
            final int sumWeight
    ) {
        TimeSlot startTime = timeBlocks.get(timeBlockIdx).timeSlot();
        TimeSlot endTime = TimeSlot.getTimeSlot(startTime.ordinal() + needTimeBlockCount);

        return new BestMeetingTimeVo(
                date,
                startTime,
                endTime,
                sumWeight
        );
    }
}
