package com.asap.server.service;


import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final TimeBlockRepository timeBlockRepository;

    @Transactional
    public TimeBlock searchTimeBlock(final TimeSlot timeSlot,
                                     final AvailableDate availableDate,
                                     final int weight) {
        TimeBlock timeBlock = timeBlockRepository.findByAvailableDateAndTimeSlot(availableDate, timeSlot)
                .orElseGet(() -> create(timeSlot, availableDate));

        timeBlock.addWeight(weight);
        timeBlockRepository.save(timeBlock);
        return timeBlock;
    }

    public List<TimeBlock> findByAvailableDate(final AvailableDate availableDate) {
        List<TimeBlock> timeBlocks = timeBlockRepository.findByAvailableDate(availableDate);
        if (timeBlocks == null) {
            throw new NotFoundException(Error.TIME_BLOCK_NOT_FOUND_EXCEPTION);
        }
        return timeBlocks;
    }

    private TimeBlock create(final TimeSlot timeSlot, final AvailableDate availableDate) {
        TimeBlock timeBlock = TimeBlock.builder()
                .timeSlot(timeSlot)
                .availableDate(availableDate)
                .build();

        timeBlockRepository.save(timeBlock);
        return timeBlock;
    }

    public List<TimeBlock> getTimeBlocksByAvailableDate(final AvailableDate availableDate) {
        return timeBlockRepository.findByAvailableDate(availableDate);
    }
}
