package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MeetingRepository extends Repository<Meeting, Long> {
    void save(final Meeting meeting);
    Optional<Meeting> findById(final Long id);
}
