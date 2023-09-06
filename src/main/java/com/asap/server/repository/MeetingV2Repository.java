package com.asap.server.repository;

import com.asap.server.domain.MeetingV2;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MeetingV2Repository extends Repository<MeetingV2, Long> {
    Optional<MeetingV2> findById(Long id);

    MeetingV2 save(MeetingV2 meetingV2);
}
