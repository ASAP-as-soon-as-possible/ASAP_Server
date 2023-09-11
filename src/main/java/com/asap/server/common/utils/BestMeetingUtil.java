package com.asap.server.common.utils;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
public class BestMeetingUtil {
    public List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlocksByDateVo> timeBlocksByDates,
            final Duration duration,
            final int totalUserCount
    ) {
        List<PossibleTimeCaseVo> timeCases = getAllPossibleMeetingTimeCases(duration, totalUserCount);
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();

        for (PossibleTimeCaseVo timeCase : timeCases) {
            timeBlocksByDates.forEach(timeBlocksByDate -> {
                bestMeetingTimes.addAll(
                        searchBestMeetingTime(timeBlocksByDate, timeCase.getDuration().getNeedBlock(), timeCase.getMemberCnt())
                );
            });
            if (bestMeetingTimes.size() > 2) return bestMeetingTimes.subList(0, 3);
        }

        while (bestMeetingTimes.size() < 3) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }

    public List<BestMeetingTimeVo> searchBestMeetingTime(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount) {
        List<TimeBlockVo> sortedTimeBlocks = filterByUserCountAndSortByTime(timeBlocksByDate.getTimeBlocks(), userCount);

        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        int endIndex = sortedTimeBlocks.size() - needTimeBlockCount;
        for (int timeBlockIdx = 0; timeBlockIdx < endIndex; timeBlockIdx++) {
            if (!isBestMeetingTime(sortedTimeBlocks, timeBlockIdx, needTimeBlockCount)) continue;

            BestMeetingTimeVo bestMeetingTime = new BestMeetingTimeVo(
                    timeBlocksByDate.getDate(),
                    sortedTimeBlocks.get(timeBlockIdx).getTimeSlot(),
                    sortedTimeBlocks.get(timeBlockIdx + needTimeBlockCount).getTimeSlot(),
                    sortedTimeBlocks.get(timeBlockIdx).getUsers()
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
        for (int i = timeBlockIdx + 1; i <= timeBlockIdx + needTimeBlockCount; i++) {
            if (nextTime.ordinal() + 1 != timeBlocks.get(i).getTimeSlot().ordinal()) {
                isBestMeetingTime = false;
                break;
            }
            nextTime = timeBlocks.get(i).getTimeSlot();
        }
        return isBestMeetingTime;
    }

    private List<PossibleTimeCaseVo> getAllPossibleMeetingTimeCases(final Duration duration, final int totalUserCount) {
        int userCount = totalUserCount;
        Duration[] durations = Duration.values();
        List<PossibleTimeCaseVo> timeCases = new ArrayList<>();
        while (userCount > 0) {
            timeCases.addAll(getPossibleMeetingTimeCases(durations, duration, userCount));
            userCount = userCount / 2;
        }
        return timeCases;
    }

    private List<PossibleTimeCaseVo> getPossibleMeetingTimeCases(final Duration[] durations, final Duration duration, final int userCount) {
        List<PossibleTimeCaseVo> timeCases = new ArrayList<>();
        for (int count = userCount; count > userCount / 2; count--) {
            timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal()], count));
            if (duration.ordinal() > 0)
                timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal() - 1], count));
        }

        int secondDuration = (duration.ordinal() >= 2) ? duration.ordinal() - 2 : -1;

        for (int durationCount = secondDuration; durationCount > -1; durationCount--) {
            for (int count = userCount; count > userCount / 2; count--) {
                timeCases.add(new PossibleTimeCaseVo(durations[durationCount], count));
            }
        }

        return timeCases;
    }
}
