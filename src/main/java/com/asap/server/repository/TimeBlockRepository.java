package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.enums.TimeSlot;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.List;

public interface TimeBlockRepository extends Repository<TimeBlock, Long> {

    void save(final TimeBlock timeBlock);

    List<TimeBlock> findByAvailableDate(final AvailableDate availableDate);
    Optional<TimeBlock> findByAvailableDateAndTimeSlot(final AvailableDate availableDate, TimeSlot timeSlot);
    List<TimeBlock> findByAvailableDate(final AvailableDate availableDate);
}
