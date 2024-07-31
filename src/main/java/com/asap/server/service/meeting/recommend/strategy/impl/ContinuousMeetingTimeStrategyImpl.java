package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContinuousMeetingTimeStrategyImpl implements ContinuousMeetingTimeStrategy {
    @Override
    public List<BestMeetingTimeVo> find(List<TimeBlockDto> timeBlocks, Duration duration) {
        LocalDate date = timeBlocks.get(0).availableDate();
        int startIdx = 0;
        int endIdx = 1;
        List<BestMeetingTimeVo> response = new ArrayList<>();
        while (endIdx < timeBlocks.size()) {
            TimeBlockDto nextTimeBlock = timeBlocks.get(endIdx);
            if (date.isEqual(nextTimeBlock.availableDate())
                    && timeBlocks.get(endIdx - 1).timeSlot().ordinal() + 1 == nextTimeBlock.timeSlot().ordinal()) {
                endIdx++;
                continue;
            }

            TimeBlockDto startTimeBlock = timeBlocks.get(startIdx);
            TimeBlockDto endTimeBlock = timeBlocks.get(endIdx - 1);
            int blockCnt = endTimeBlock.timeSlot().ordinal() - startTimeBlock.timeSlot().ordinal();

            if (blockCnt >= duration.getNeedBlock()) {
                int weight = timeBlocks
                        .subList(startIdx, endIdx - 1)
                        .stream()
                        .map(TimeBlockDto::weight)
                        .reduce(0, Integer::sum);

                response.add(
                        new BestMeetingTimeVo(date, startTimeBlock.timeSlot(), endTimeBlock.timeSlot(), weight));
            }
            startIdx = endIdx++;
            date = nextTimeBlock.availableDate();

        }
        if (startIdx < endIdx) {
            TimeBlockDto startTimeBlock = timeBlocks.get(startIdx);
            TimeBlockDto endTimeBlock = timeBlocks.get(endIdx - 1);
            int blockCnt = endTimeBlock.timeSlot().ordinal() - startTimeBlock.timeSlot().ordinal();

            if (blockCnt >= duration.getNeedBlock()) {
                int weight = timeBlocks
                        .subList(startIdx, endIdx - 1)
                        .stream()
                        .map(TimeBlockDto::weight)
                        .reduce(0, Integer::sum);

                response.add(new BestMeetingTimeVo(date, startTimeBlock.timeSlot(), endTimeBlock.timeSlot(), weight));
            }
        }
        return response;
    }
}
