package com.asap.server.common.utils.strategy;

import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;

import java.util.List;

public interface FindBestMeetingTimeStrategy {
    List<BestMeetingTimeVo> find(final TimeBlocksByDateVo timeBlocksByDate, final int needTimeBlockCount, final int userCount);
}
