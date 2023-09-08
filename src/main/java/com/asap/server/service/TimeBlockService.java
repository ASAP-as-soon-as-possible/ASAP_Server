package com.asap.server.service;


import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final TimeBlockRepository timeBlockRepository;

    @Transactional
    public TimeBlock searchTimeBlock(final TimeSlot timeSlot, final AvailableDate availableDate) {
        TimeBlock timeBlock = timeBlockRepository.findByAvailableDateAndTimeSlot(availableDate, timeSlot)
                .orElse(
                        create(timeSlot, availableDate)
                );
        timeBlockRepository.save(timeBlock);
        return timeBlock;
    }

    private TimeBlock create(final TimeSlot timeSlot, final AvailableDate availableDate) {
        TimeBlock timeBlock = TimeBlock.builder()
                .timeSlot(timeSlot)
                .availableDate(availableDate)
                .build();

        timeBlockRepository.save(timeBlock);
        return timeBlock;
    }
}
