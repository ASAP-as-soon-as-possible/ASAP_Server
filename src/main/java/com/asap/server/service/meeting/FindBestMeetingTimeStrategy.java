package com.asap.server.service.meeting;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FindBestMeetingTimeStrategy {
    public List<BestMeetingTimeVo> find(
            final List<TimeBlockDto> timeBlocks,
            final PossibleTimeCaseVo timeCase
    ) {
        int needBlock = timeCase.getDuration().getNeedBlock();
        int userCount = timeCase.getMemberCnt();

        List<TimeBlockDto> sortedTimeBlocks = timeBlocks.stream()
                .filter(timeBlock -> timeBlock.userCount() == userCount)
                .sorted(TimeBlockDto::compareTo)
                .collect(Collectors.toList());

        return getBestMeetingTime(sortedTimeBlocks, needBlock);
    }

    private List<BestMeetingTimeVo> getBestMeetingTime(
            final List<TimeBlockDto> timeBlocks,
            final int needTimeBlockCount
    ) {
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();

        int endIndex = timeBlocks.size() - needTimeBlockCount + 1;
        for (int timeBlockIdx = 0; timeBlockIdx < endIndex; timeBlockIdx++) {
            int endIdx = timeBlockIdx + needTimeBlockCount;

            if (!isBestMeetingTime(timeBlocks, timeBlockIdx, endIdx)) {
                continue;
            }

            int sumWeight = sumTimeBlocksWeight(timeBlocks, timeBlockIdx, endIdx);

            BestMeetingTimeVo bestMeetingTime = createBestMeetingTimeVo(
                    timeBlocks,
                    needTimeBlockCount,
                    timeBlockIdx,
                    sumWeight
            );

            bestMeetingTimes.add(bestMeetingTime);
        }
        return bestMeetingTimes
                .stream()
                .sorted(Comparator.comparing(BestMeetingTimeVo::weight, Comparator.reverseOrder()))
                .toList();
    }

    private BestMeetingTimeVo createBestMeetingTimeVo(
            List<TimeBlockDto> timeBlocks,
            int needTimeBlockCount,
            int timeBlockIdx,
            int sumWeight
    ) {
        TimeBlockDto timeBlock = timeBlocks.get(timeBlockIdx);

        TimeSlot startTime = timeBlock.timeSlot();
        TimeSlot endTime = TimeSlot.getTimeSlot(startTime.ordinal() + needTimeBlockCount);

        return new BestMeetingTimeVo(
                timeBlock.availableDate(),
                startTime,
                endTime,
                sumWeight
        );
    }

    private boolean isBestMeetingTime(
            final List<TimeBlockDto> timeBlocks,
            final int timeBlockIdx,
            final int endIdx
    ) {
        boolean isBestMeetingTime = true;
        TimeSlot nextTime = timeBlocks.get(timeBlockIdx).timeSlot();
        for (int i = timeBlockIdx + 1; i < endIdx; i++) {
            if (nextTime.ordinal() + 1 != timeBlocks.get(i).timeSlot().ordinal()) {
                return false;
            }

            nextTime = timeBlocks.get(i).timeSlot();
        }
        return isBestMeetingTime;
    }

    private int sumTimeBlocksWeight(
            final List<TimeBlockDto> timeBlocks,
            final int startIdx,
            final int endIdx
    ) {
        return timeBlocks
                .subList(startIdx, endIdx)
                .stream()
                .map(TimeBlockDto::weight)
                .reduce(0, Integer::sum);
    }
}
