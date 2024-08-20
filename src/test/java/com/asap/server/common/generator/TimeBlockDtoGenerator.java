package com.asap.server.common.generator;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.time.vo.TimeBlock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeBlockDtoGenerator {
    private static final TimeSlot[] timeSlots = TimeSlot.values();

    public static List<TimeBlockDto> generator(
            LocalDate availableDate,
            TimeSlot startTime,
            TimeSlot endTime,
            int weight,
            long userCount
    ) {
        List<TimeBlockDto> timeBlocks = new ArrayList<>();
        for (int i = startTime.ordinal(); i <= endTime.ordinal(); i++) {
            timeBlocks.add(new TimeBlockDto(availableDate, timeSlots[i], weight, userCount));
        }
        return timeBlocks;
    }
}
