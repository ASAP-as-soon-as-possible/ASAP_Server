package com.asap.server.repository.meeting;

import com.asap.server.domain.Meeting;

import java.util.Optional;

public interface MeetingRepositoryCustom {
    Optional<Meeting> findByIdWithHost(final Long id);
}
