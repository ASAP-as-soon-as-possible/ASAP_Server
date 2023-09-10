package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(final AvailableDate availableDate);

    List<AvailableDate> findByMeeting(final MeetingV2 meetingV2);

    Optional<AvailableDate> findByMeetingAndDate(final MeetingV2 meetingV2, final LocalDate date);
}
