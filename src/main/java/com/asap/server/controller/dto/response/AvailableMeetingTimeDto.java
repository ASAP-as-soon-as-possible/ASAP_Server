package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.TimeSlot;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AvailableMeetingTimeDto {
    private String date;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private int weight;
    private List<String> userNames;
}
