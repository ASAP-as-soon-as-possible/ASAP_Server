package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BestMeetingTimeVo {
    private LocalDate date;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private List<UserVo> users;
}
