package com.asap.server.persistence.repository.meeting;

import com.asap.server.persistence.domain.Meeting;

import java.util.Optional;

public interface MeetingRepositoryCustom {
    Optional<Meeting> findByIdWithHost(final Long id);
}
