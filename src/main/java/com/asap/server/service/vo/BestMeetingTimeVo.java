package com.asap.server.service.vo;

import com.asap.server.persistence.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public record BestMeetingTimeVo(LocalDate date, TimeSlot startTime, TimeSlot endTime, int weight, List<Long> userIds) {
}
