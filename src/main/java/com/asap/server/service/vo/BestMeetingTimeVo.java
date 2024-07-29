package com.asap.server.service.vo;

import com.asap.server.persistence.domain.enums.TimeSlot;

import java.time.LocalDate;

public record BestMeetingTimeVo(LocalDate date, TimeSlot startTime, TimeSlot endTime, int weight) {
}
