package com.asap.server.persistence.repository.meeting;

import com.asap.server.persistence.domain.Meeting;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MeetingRepository extends Repository<Meeting, Long>, MeetingRepositoryCustom {
    Optional<Meeting> findById(final Long id);

    Meeting save(final Meeting meeting);
}
