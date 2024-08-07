package com.asap.server.persistence.repository.user;

import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.vo.UserVo;

import java.time.LocalDate;
import java.util.List;

public interface UserRepositoryCustom {
    void updateUserIsFixedByMeeting(final Meeting meeting, final List<Long> users);

    List<UserVo> findByAvailableDateAndTimeSlots(Long meetingId, LocalDate availableDate, List<TimeSlot> timeSlots);
}
