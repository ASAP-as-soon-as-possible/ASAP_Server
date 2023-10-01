package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.Meeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(final AvailableDate availableDate);

    List<AvailableDate> findByMeeting(final Meeting meeting);

    Optional<AvailableDate> findByMeetingAndDate(final Meeting meeting, final LocalDate date);

    @Modifying
    @Query("delete from AvailableDate a where a.meeting = :meeting")
    void deleteByMeeting(@Param("meeting") final Meeting meeting);
}
