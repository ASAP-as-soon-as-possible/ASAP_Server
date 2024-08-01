package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ContinuousMeetingTimeStrategyImpl implements ContinuousMeetingTimeStrategy {
    @Override
    public List<BestMeetingTimeVo> find(List<TimeBlockDto> timeBlocks, Duration duration) {
        int startIdx = 0;
        int endIdx = 1;

        List<BestMeetingTimeVo> response = new ArrayList<>();
        while (endIdx < timeBlocks.size()) {
            TimeBlockDto endTimeBlock = timeBlocks.get(endIdx - 1);
            TimeBlockDto nextTimeBlock = timeBlocks.get(endIdx);

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
                .collect(Collectors.toList());
    }

    private void validateAndAddMeetingTime(
            List<TimeBlockDto> timeBlocks,
            Duration duration,
            int startIdx,
            int endIdx,
            List<BestMeetingTimeVo> response
    ) {
        TimeBlockDto startTimeBlock = timeBlocks.get(startIdx);
        TimeBlockDto endTimeBlock = timeBlocks.get(endIdx - 1);
        if (isSatisfiedDuration(startTimeBlock, endTimeBlock, duration)) {
            int weight = sumTimeBlocksWeight(timeBlocks, startIdx, endIdx);
            TimeSlot endTimeSlot = TimeSlot.getTimeSlot(endTimeBlock.timeSlot().ordinal() + 1);
            response.add(
                    new BestMeetingTimeVo(
                            startTimeBlock.availableDate(),
                            startTimeBlock.timeSlot(),
                            endTimeSlot,
                            weight
                    )
            );
        }
    }

    private boolean isContinuous(
            TimeBlockDto endTimeBlock,
            TimeBlockDto nextTimeBlock
    ) {
        return endTimeBlock.availableDate().isEqual(nextTimeBlock.availableDate())
                && endTimeBlock.timeSlot().ordinal() + 1 == nextTimeBlock.timeSlot().ordinal();
    }

    private boolean isSatisfiedDuration(
            TimeBlockDto startTimeBlock,
            TimeBlockDto endTimeBlock,
            Duration duration
    ) {
        int blockCnt = endTimeBlock.timeSlot().ordinal() - startTimeBlock.timeSlot().ordinal();
        return blockCnt + 1 >= duration.getNeedBlock();
    }

    private int sumTimeBlocksWeight(
            final List<TimeBlockDto> timeBlocks,
            final int startIdx,
            final int endIdx
    ) {
        int totalWeight = timeBlocks.subList(startIdx, endIdx).stream()
                .mapToInt(TimeBlockDto::weight)
                .sum();
        return totalWeight / (endIdx - startIdx);
    }
}
