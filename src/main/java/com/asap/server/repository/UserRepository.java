package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    User save(final User user);

    @Modifying
    @Query("update User u set u.isFixed = true where u.meeting = :meeting and u.id in :userIds")
    void updateUserIsFixedByMeeting(@Param("meeting") final Meeting meeting, @Param("userIds") final List<Long> users);

    List<User> findByMeetingAndIsFixed(final Meeting meeting, final boolean isFixed);

    Optional<User> findById(final Long id);

    List<User> findByMeeting(final Meeting meeting);

    int countByMeeting(final Meeting meeting);
}
