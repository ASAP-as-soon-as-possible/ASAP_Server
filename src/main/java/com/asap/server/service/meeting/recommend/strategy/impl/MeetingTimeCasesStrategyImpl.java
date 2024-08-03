package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.meeting.recommend.strategy.MeetingTimeCasesStrategy;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.asap.server.persistence.domain.enums.Duration.HOUR;
import static com.asap.server.persistence.domain.enums.Duration.HOUR_HALF;
import static com.asap.server.persistence.domain.enums.Duration.TWO_HOUR;

@Component
public class MeetingTimeCasesStrategyImpl implements MeetingTimeCasesStrategy {
    private static final Duration[] durations = Duration.values();

    @Override
    public List<PossibleTimeCaseVo> find(final Duration duration, int userCount) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>();
        final int standard = (int) (userCount * 0.2);

        if (duration.getNeedBlock() > TWO_HOUR.getNeedBlock()) {
            possibleTimeCases.addAll(getPossibleTimeCasesOverTwoHourOverStandardRatio(duration, userCount, standard));
            userCount -= standard + 1;

            while (userCount >= 2) {
                possibleTimeCases.addAll(findPossibleTimeCasesOverTwoHourUnderStandardRatio(duration, userCount, standard));
                userCount -= standard + 1;
            }

        } else {

            do {
                possibleTimeCases.addAll(findPossibleTimeUnderTwoHour(duration, userCount, standard));
                userCount -= standard + 1;
            } while (userCount >= 2);

        }
        return possibleTimeCases;
    }

    private List<PossibleTimeCaseVo> getPossibleTimeCasesOverTwoHourOverStandardRatio(final Duration duration, final int userCount, final int standard) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>(findPossibleTimeCasesUnitOverTwoHour(duration, userCount, standard));

        Duration nextDuration = duration;

        while (nextDuration.getNeedBlock() > HOUR.getNeedBlock() && nextDuration.getNeedBlock() >= duration.getNeedBlock() - TWO_HOUR.getNeedBlock()) {
            nextDuration = durations[nextDuration.getNeedBlock() - 3];
            possibleTimeCases.addAll(findPossibleTimeCasesUnitOverTwoHour(nextDuration, userCount, standard));
        }

        return possibleTimeCases;
    }

    private List<PossibleTimeCaseVo> findPossibleTimeCasesOverTwoHourUnderStandardRatio(final Duration duration, final int userCount, final int standard) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>();
        Duration nextDuration = duration;

        while (nextDuration.getNeedBlock() > duration.getNeedBlock() - HOUR.getNeedBlock() && nextDuration.getNeedBlock() > HOUR.getNeedBlock()) {
            possibleTimeCases.addAll(findPossibleTimeCasesUnitOverTwoHour(nextDuration, userCount, standard));
            nextDuration = durations[nextDuration.getNeedBlock() - 3];
        }

        for (int count = userCount; count >= userCount - standard; count--) {
            possibleTimeCases.add(new PossibleTimeCaseVo(nextDuration, count));
        }

        return possibleTimeCases;
    }


    private List<PossibleTimeCaseVo> findPossibleTimeUnderTwoHour(final Duration duration, final int userCount, final int standard) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>(findPossibleTimeCasesUnitUnderTwoHour(duration, userCount, standard));

        Duration nextDuration = duration;

        while (nextDuration.getNeedBlock() > HOUR.getNeedBlock()) {
            nextDuration = durations[nextDuration.getNeedBlock() - 3];
            possibleTimeCases.addAll(findPossibleTimeCasesUnitUnderTwoHour(nextDuration, userCount, standard));
        }

        return possibleTimeCases;
    }

    private List<PossibleTimeCaseVo> findPossibleTimeCasesUnitOverTwoHour(final Duration duration, final int userCount, final int standard) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>();

        for (int count = userCount; count >= userCount - standard; count--) {
            possibleTimeCases.add(new PossibleTimeCaseVo(duration, count));

            if (duration.getNeedBlock() < HOUR_HALF.getNeedBlock()) continue;

            possibleTimeCases.add(new PossibleTimeCaseVo(durations[duration.getNeedBlock() - 2], count));

        }
        return possibleTimeCases;
    }

    private List<PossibleTimeCaseVo> findPossibleTimeCasesUnitUnderTwoHour(final Duration duration, final int userCount, final int standard) {
        List<PossibleTimeCaseVo> possibleTimeCases = new ArrayList<>();

        for (int count = userCount; count >= userCount - standard; count--) {
            possibleTimeCases.add(new PossibleTimeCaseVo(duration, count));

            if (duration.getNeedBlock() < HOUR.getNeedBlock()) continue;

            possibleTimeCases.add(new PossibleTimeCaseVo(durations[duration.getNeedBlock() - 2], count));

        }
        return possibleTimeCases;
    }
}