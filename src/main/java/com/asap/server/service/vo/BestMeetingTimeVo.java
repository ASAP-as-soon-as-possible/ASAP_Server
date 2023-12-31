package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;

import java.time.LocalDate;

public record BestMeetingTimeVo(Long availableDateId, LocalDate date, TimeSlot startTime, TimeSlot endTime, int weight) {
}
