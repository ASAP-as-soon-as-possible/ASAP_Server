package com.asap.server.common.utils.strategy.impl;

import com.asap.server.common.utils.strategy.FindBestMeetingTimeCasesStrategy;
import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindBestMeetingTimeCasesStrategyImpl implements FindBestMeetingTimeCasesStrategy {
    private static final Duration[] durations = Duration.values();

    @Override
    public List<PossibleTimeCaseVo> find(final Duration duration, int userCount) {
        List<PossibleTimeCaseVo> timeCases = new ArrayList<>();
        while (userCount > 0) {
            List<PossibleTimeCaseVo> possibleMeetingTimeCases = getPossibleMeetingTimeCases(duration, userCount);
            timeCases.addAll(possibleMeetingTimeCases);

            userCount = userCount / 2;
        }
        return timeCases;
    }

    private List<PossibleTimeCaseVo> getPossibleMeetingTimeCases(final Duration duration, final int userCount) {
        List<PossibleTimeCaseVo> firstTimeCases = findFirstPossibleMeetingTimeCases(duration, userCount);

        if (duration.ordinal() < 2) return firstTimeCases;

        List<PossibleTimeCaseVo> secondTimeCases = findSecondPossibleMeetingTimeCases(duration, userCount);
        firstTimeCases.addAll(secondTimeCases);
        return firstTimeCases;
    }

    private List<PossibleTimeCaseVo> findFirstPossibleMeetingTimeCases(final Duration duration, final int userCount) {
        List<PossibleTimeCaseVo> timeCases = new ArrayList<>();

        for (int count = userCount; count > userCount / 2; count--) {
            timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal()], count));

            if (duration.ordinal() < 1) continue;

            timeCases.add(new PossibleTimeCaseVo(durations[duration.ordinal() - 1], count));
        }

        return timeCases;
    }

    private List<PossibleTimeCaseVo> findSecondPossibleMeetingTimeCases(final Duration duration, final int userCount) {
        List<PossibleTimeCaseVo> timeCases = new ArrayList<>();

        int secondDuration = duration.ordinal() - 2;
        for (int durationCount = secondDuration; durationCount > -1; durationCount--) {
            for (int count = userCount; count > userCount / 2; count--) {
                timeCases.add(new PossibleTimeCaseVo(durations[durationCount], count));
            }
        }

        return timeCases;
    }
}
