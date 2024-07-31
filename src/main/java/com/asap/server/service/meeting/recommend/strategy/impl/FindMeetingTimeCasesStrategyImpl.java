package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.meeting.recommend.strategy.FindMeetingTimeCasesStrategy;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FindMeetingTimeCasesStrategyImpl implements FindMeetingTimeCasesStrategy {
    @Override
    public List<PossibleTimeCaseVo> find(Duration duration, int userCount) {
        return null;
    }
}
