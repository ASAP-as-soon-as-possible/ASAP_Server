package com.asap.server.service.meeting.recommend;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.meeting.recommend.strategy.BestMeetingTimeStrategy;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.meeting.recommend.strategy.MeetingTimeCasesStrategy;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingTimeRecommendService {
    private static final int BEST_MEETING_TIME_SIZE = 3;
    private final MeetingTimeCasesStrategy meetingTimeTimeCasesStrategy;
    private final ContinuousMeetingTimeStrategy continuousMeetingTimeStrategy;
    private final BestMeetingTimeStrategy bestMeetingTimeStrategy;

    public List<BestMeetingTimeVo> getBestMeetingTime(
            List<TimeBlockVo> timeBlocks,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = meetingTimeTimeCasesStrategy.find(duration, userCount);

        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (PossibleTimeCaseVo timeCase : timeCases) {
            List<TimeBlockVo> timeBlocksFilteredUserCount = timeBlocks.stream()
                    .filter(t -> t.userIds().size() == timeCase.memberCnt())
                    .toList();

            List<BestMeetingTimeVo> candidateMeetingTimes =
                    continuousMeetingTimeStrategy.find(timeBlocksFilteredUserCount, timeCase.duration());

            timeBlocks = timeBlocks.stream()
                    .filter(timeBlock -> candidateMeetingTimes
                            .stream()
                            .noneMatch(candidateMeetingTime -> isRecommendedMeetingTime(timeBlock, candidateMeetingTime))
                    )
                    .toList();

            bestMeetingTimes.addAll(bestMeetingTimeStrategy.find(candidateMeetingTimes, timeCase.duration()));

            if (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
                continue;
            }

            return bestMeetingTimes.stream().limit(BEST_MEETING_TIME_SIZE).toList();
        }

        while (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }

    private boolean isRecommendedMeetingTime(TimeBlockVo timeBlock, BestMeetingTimeVo bestMeetingTime) {
        return timeBlock.availableDate().isEqual(bestMeetingTime.date())
                && bestMeetingTime.startTime().getIndex() <= timeBlock.timeSlot().getIndex()
                && bestMeetingTime.endTime().getIndex() >= timeBlock.timeSlot().getIndex();
    }
}
