package com.asap.server.service.meeting.recommend.strategy;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.List;

public interface ContinuousMeetingTimeStrategy {
    List<BestMeetingTimeVo> find(List<TimeBlockVo> timeBlocks, Duration duration);
}
