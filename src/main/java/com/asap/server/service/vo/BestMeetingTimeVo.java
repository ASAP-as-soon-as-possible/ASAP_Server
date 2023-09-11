package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BestMeetingTimeVo {
    private TimeSlot startTime;
    private TimeSlot endTime;
    private List<UserVo> users;
}
