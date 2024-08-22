package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContinuousMeetingTimeStrategyImpl implements ContinuousMeetingTimeStrategy {
    @Override
    public List<BestMeetingTimeVo> find(List<TimeBlockVo> timeBlocks, Duration duration) {
        List<BestMeetingTimeVo> response = new ArrayList<>();
        if (timeBlocks.isEmpty()) {
            return response;
        }

        int startIdx = 0;
        int endIdx = 1;

        while (endIdx < timeBlocks.size()) {
            TimeBlockVo endTimeBlock = timeBlocks.get(endIdx - 1);
            TimeBlockVo nextTimeBlock = timeBlocks.get(endIdx);

            if (isContinuous(endTimeBlock, nextTimeBlock)) {
                endIdx++;
                continue;
            }

            validateAndAddMeetingTime(
                    timeBlocks,
                    duration,
                    startIdx,
                    endIdx,
                    response
            );

            startIdx = endIdx++;
        }

        if (startIdx < endIdx) {
            validateAndAddMeetingTime(
                    timeBlocks,
                    duration,
                    startIdx,
                    endIdx,
                    response
            );
        }
        return response.stream()
                .sorted((t1, t2) -> t2.weight() - t1.weight())
                .toList();
    }

    private void validateAndAddMeetingTime(
            List<TimeBlockVo> timeBlocks,
            Duration duration,
            int startIdx,
            int endIdx,
            List<BestMeetingTimeVo> response
    ) {
        TimeBlockVo startTimeBlock = timeBlocks.get(startIdx);
        TimeBlockVo endTimeBlock = timeBlocks.get(endIdx - 1);
        if (isSatisfiedDuration(startTimeBlock, endTimeBlock, duration)) {
            int weight = sumTimeBlocksWeight(timeBlocks, startIdx, endIdx);
            List<Long> userIds = findUserIdsBetween(timeBlocks, startIdx, endIdx);
            TimeSlot endTimeSlot = TimeSlot.getTimeSlot(endTimeBlock.timeSlot().getIndex() + 1);
            response.add(
                    new BestMeetingTimeVo(
                            startTimeBlock.availableDate(),
                            startTimeBlock.timeSlot(),
                            endTimeSlot,
                            weight,
                            userIds
                    )
            );
        }
    }

    private boolean isContinuous(
            TimeBlockVo endTimeBlock,
            TimeBlockVo nextTimeBlock
    ) {
        return endTimeBlock.availableDate().isEqual(nextTimeBlock.availableDate())
                && endTimeBlock.timeSlot().getIndex() + 1 == nextTimeBlock.timeSlot().getIndex();
    }

    private boolean isSatisfiedDuration(
            TimeBlockVo startTimeBlock,
            TimeBlockVo endTimeBlock,
            Duration duration
    ) {
        int blockCnt = endTimeBlock.timeSlot().getIndex() - startTimeBlock.timeSlot().getIndex();
        return blockCnt + 1 >= duration.getNeedBlock();
    }

    private int sumTimeBlocksWeight(
            final List<TimeBlockVo> timeBlocks,
            final int startIdx,
            final int endIdx
    ) {
        int totalWeight = timeBlocks.subList(startIdx, endIdx).stream()
                .mapToInt(TimeBlockVo::weight)
                .sum();
        return totalWeight / (endIdx - startIdx);
    }

    private List<Long> findUserIdsBetween(
            final List<TimeBlockVo> timeBlocks,
            final int startIdx,
            final int endIdx
    ) {
        return timeBlocks.subList(startIdx, endIdx).stream()
                .flatMap(timeBlock -> timeBlock.userIds().stream())
                .distinct()
                .toList();
    }
}
