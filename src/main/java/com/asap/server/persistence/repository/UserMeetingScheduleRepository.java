package com.asap.server.persistence.repository;

import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface UserMeetingScheduleRepository extends Repository<UserMeetingSchedule, Long> {
    void save(final UserMeetingSchedule userMeetingSchedule);

    List<UserMeetingSchedule> findAllByMeetingId(final long meetingId);
}
