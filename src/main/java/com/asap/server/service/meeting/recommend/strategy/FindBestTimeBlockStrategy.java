package com.asap.server.service.meeting.recommend.strategy;

import com.asap.server.service.vo.BestMeetingTimeVo;
import java.util.List;

public interface FindBestTimeBlockStrategy {
    List<BestMeetingTimeVo> find(List<BestMeetingTimeVo> candidateMeetingTimes);
}
