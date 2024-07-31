package com.asap.server.service.meeting.recommend.strategy.impl;

import com.asap.server.service.meeting.recommend.strategy.FindBestTimeBlockStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FindBestTimeBlockStrategyImpl implements FindBestTimeBlockStrategy {
    @Override
    public List<BestMeetingTimeVo> find(List<BestMeetingTimeVo> candidateMeetingTimes) {
        return null;
    }
}
