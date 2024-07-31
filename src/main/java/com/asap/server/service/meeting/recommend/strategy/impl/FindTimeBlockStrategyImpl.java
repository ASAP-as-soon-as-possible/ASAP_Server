package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.FindTimeBlockStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FindTimeBlockStrategyImpl implements FindTimeBlockStrategy {
    @Override
    public List<BestMeetingTimeVo> find(List<TimeBlockDto> timeBlocksFilteredUserCount, Duration duration) {
        return null;
    }
}