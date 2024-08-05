package com.asap.server.service.meeting.recommend;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.BestMeetingTimeStrategy;
import com.asap.server.service.meeting.recommend.strategy.MeetingTimeCasesStrategy;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
            final List<TimeBlockDto> timeBlocks,
            final Duration duration,
            final int userCount
    ) {
        List<PossibleTimeCaseVo> timeCases = meetingTimeTimeCasesStrategy.find(duration, userCount);

        List<BestMeetingTimeVo> bestMeetingTimes = new ArrayList<>();
        for (PossibleTimeCaseVo timeCase : timeCases) {
            List<TimeBlockDto> timeBlocksFilteredUserCount = timeBlocks.stream()
                    .filter(t -> t.userCount() == timeCase.memberCnt())
                    .toList();

            List<BestMeetingTimeVo> candidateMeetingTimes = new ArrayList<>(
                    continuousMeetingTimeStrategy.find(timeBlocksFilteredUserCount, timeCase.duration()));
            candidateMeetingTimes = bestMeetingTimeStrategy.find(candidateMeetingTimes, timeCase.duration());
            bestMeetingTimes.addAll(candidateMeetingTimes);

            if (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
                continue;
            }

            return bestMeetingTimes.stream().limit(BEST_MEETING_TIME_SIZE).collect(Collectors.toList());
        }

        while (bestMeetingTimes.size() < BEST_MEETING_TIME_SIZE) {
            bestMeetingTimes.add(null);
        }
        return bestMeetingTimes;
    }
}
