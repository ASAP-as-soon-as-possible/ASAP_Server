package com.asap.server.service.meeting.recommend.strategy;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.List;

public interface MeetingTimeTimeCasesStrategy {
    List<PossibleTimeCaseVo> find(final Duration duration, int userCount);
}
