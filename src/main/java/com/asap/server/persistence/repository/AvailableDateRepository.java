package com.asap.server.persistence.repository;

import com.asap.server.persistence.domain.AvailableDate;
import com.asap.server.persistence.domain.Meeting;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(final AvailableDate availableDate);

    List<AvailableDate> findByMeeting(final Meeting meeting);

    Optional<AvailableDate> findByMeetingAndDate(final Meeting meeting, final LocalDate date);
}
