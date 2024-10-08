package com.asap.server.service.time.strategy;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.time.vo.BestMeetingTimeVo;
import java.util.List;

public interface BestMeetingTimeStrategy {
    List<BestMeetingTimeVo> find(List<BestMeetingTimeVo> candidateMeetingTimes, Duration duration);
}
