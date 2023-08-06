package com.asap.server.repository;

import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeetingTimeRepository extends JpaRepository<MeetingTime, Long> {
    List<MeetingTime> findByUser(User user);
}
