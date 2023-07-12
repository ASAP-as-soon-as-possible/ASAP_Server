package com.asap.server.repository;

import com.asap.server.domain.MeetingTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingTimeRepository extends JpaRepository<MeetingTime, Long> {
}
