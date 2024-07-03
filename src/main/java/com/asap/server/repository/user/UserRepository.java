package com.asap.server.repository.user;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, UserRepositoryCustom {
    User save(final User user);

    List<User> findByMeetingAndIsFixed(final Meeting meeting, final boolean isFixed);

    Optional<User> findById(final Long id);

    List<User> findByMeeting(final Meeting meeting);

    int countByMeeting(final Meeting meeting);
}
