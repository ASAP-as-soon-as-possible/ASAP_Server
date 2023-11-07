package com.asap.server.common.utils.strategy.impl;

import com.asap.server.common.utils.strategy.FindOptimalMeetingTimeStrategy;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FindOptimalMeetingTimeStrategyImpl implements FindOptimalMeetingTimeStrategy {
    @Override
    public List<BestMeetingTimeVo> findOptimalMeetingTime(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount) {
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
