package com.asap.server.service.time.strategy;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.time.vo.PossibleTimeCaseVo;
import java.util.List;

public interface MeetingTimeCasesStrategy {
    List<PossibleTimeCaseVo> find(final Duration duration, int userCount);

}
