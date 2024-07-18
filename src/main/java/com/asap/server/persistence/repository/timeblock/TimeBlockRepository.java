package com.asap.server.persistence.repository.timeblock;

import com.asap.server.persistence.domain.AvailableDate;
import com.asap.server.persistence.domain.TimeBlock;
import com.asap.server.persistence.domain.enums.TimeSlot;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TimeBlockRepository extends Repository<TimeBlock, Long>, TimeBlockRepositoryCustom {

    void save(final TimeBlock timeBlock);

    List<TimeBlock> findByAvailableDate(final AvailableDate availableDate);

    Optional<TimeBlock> findByAvailableDateAndTimeSlot(final AvailableDate availableDate, TimeSlot timeSlot);
}
