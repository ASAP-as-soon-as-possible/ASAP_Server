package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(AvailableDate availableDate);

    List<AvailableDate> findByMeeting(MeetingV2 meetingV2);
}
