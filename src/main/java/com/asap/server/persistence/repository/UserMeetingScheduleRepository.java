package com.asap.server.persistence.repository;

import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import org.springframework.data.repository.Repository;

public interface UserMeetingScheduleRepository extends Repository<UserMeetingSchedule, Long> {
    void save(final UserMeetingSchedule userMeetingSchedule);
}
