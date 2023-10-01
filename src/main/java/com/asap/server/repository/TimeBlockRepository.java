package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.enums.TimeSlot;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TimeBlockRepository extends Repository<TimeBlock, Long> {

    void save(final TimeBlock timeBlock);

    List<TimeBlock> findByAvailableDate(final AvailableDate availableDate);

    Optional<TimeBlock> findByAvailableDateAndTimeSlot(final AvailableDate availableDate, TimeSlot timeSlot);

    List<TimeBlock> findByAvailableDateIn(final List<AvailableDate> availableDates);

    @Modifying(clearAutomatically = true)
    @Query("delete from TimeBlock t where t.availableDate in :availableDates")
    void deleteByAvailableDatesIn(@Param("availableDates") final List<AvailableDate> availableDates);
}
