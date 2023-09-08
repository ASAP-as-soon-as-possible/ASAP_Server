package com.asap.server.repository;

import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.UserV2;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserV2Repository extends Repository<UserV2, Long> {
    UserV2 save(UserV2 userV2);

    Optional<UserV2> findById(Long id);
    List<UserV2> findByMeetingAndIsFixed(final MeetingV2 meeting, final boolean isFixed);
}
