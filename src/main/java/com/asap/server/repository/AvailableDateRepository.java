package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.Meeting;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(final AvailableDate availableDate);

    List<AvailableDate> findByMeeting(final Meeting meeting);

    Optional<AvailableDate> findByMeetingAndDate(final Meeting meeting, final LocalDate date);
}
