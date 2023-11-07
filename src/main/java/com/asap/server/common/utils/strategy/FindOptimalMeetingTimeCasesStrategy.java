package com.asap.server.common.utils.strategy;

import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;

import java.util.List;

public interface FindOptimalMeetingTimeCasesStrategy {
    List<PossibleTimeCaseVo> findOptimalMeetingTimeCases(final Duration duration, final int userCount);
}
