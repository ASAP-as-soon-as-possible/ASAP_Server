package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MeetingRepository extends Repository<Meeting, Long> {
    void save(Meeting meeting);
    Optional<Meeting> findById(Long id);
}
