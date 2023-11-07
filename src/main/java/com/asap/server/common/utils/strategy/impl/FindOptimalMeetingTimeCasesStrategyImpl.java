package com.asap.server.common.utils.strategy.impl;

import com.asap.server.common.utils.strategy.FindOptimalMeetingTimeCasesStrategy;
import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindOptimalMeetingTimeCasesStrategyImpl implements FindOptimalMeetingTimeCasesStrategy {
    @Override
    public List<PossibleTimeCaseVo> findOptimalMeetingTimeCases(final Duration duration, int userCount) {
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
