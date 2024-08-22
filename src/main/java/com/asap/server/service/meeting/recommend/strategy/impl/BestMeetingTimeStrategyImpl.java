package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.meeting.recommend.strategy.BestMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BestMeetingTimeStrategyImpl implements BestMeetingTimeStrategy {
    private static final int EXTRA_BLOCKS = 4;

    @Override
    public List<BestMeetingTimeVo> find(List<BestMeetingTimeVo> candidateMeetingTimes, Duration duration) {
        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (BestMeetingTimeVo candidate : candidateMeetingTimes) {
            bestMeetingTimes.add(createFirstMeetingTime(candidate, duration));

            if (isTimeBlockSufficientlyLong(candidate, duration)) {
                bestMeetingTimes.add(createSecondMeetingTime(candidate, duration));
            }
        }
        return bestMeetingTimes;
    }

    private BestMeetingTimeVo createFirstMeetingTime(BestMeetingTimeVo candidate, Duration duration) {
        TimeSlot endTimeSlot = TimeSlot.getTimeSlot(candidate.startTime().getIndex() + duration.getNeedBlock());
        return new BestMeetingTimeVo(candidate.date(), candidate.startTime(), endTimeSlot, candidate.weight(), candidate.userIds());
    }

    private BestMeetingTimeVo createSecondMeetingTime(BestMeetingTimeVo candidate, Duration duration) {
        TimeSlot startTimeSlot = TimeSlot.getTimeSlot(candidate.endTime().getIndex() - duration.getNeedBlock());
        return new BestMeetingTimeVo(candidate.date(), startTimeSlot, candidate.endTime(), candidate.weight(), candidate.userIds());
    }

    private boolean isTimeBlockSufficientlyLong(BestMeetingTimeVo candidate, Duration duration) {
        int timeBlockLength = candidate.endTime().getIndex() - candidate.startTime().getIndex();
        return timeBlockLength >= duration.getNeedBlock() * 2 + EXTRA_BLOCKS;
    }
}
