package com.asap.server.common.utils.strategy;

import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;

import java.util.List;

public interface FindOptimalMeetingTimeStrategy {
    List<BestMeetingTimeVo> findOptimalMeetingTime(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount);
}
