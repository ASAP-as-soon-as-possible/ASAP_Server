package com.asap.server.common.utils.strategy;

import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;

import java.util.List;

public interface FindBestMeetingTimeCasesStrategy {
    List<PossibleTimeCaseVo> find(final Duration duration, final int userCount);
}
