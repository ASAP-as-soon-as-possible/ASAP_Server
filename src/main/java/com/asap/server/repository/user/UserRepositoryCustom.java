package com.asap.server.repository.user;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.UserVo;

import java.util.List;

public interface UserRepositoryCustom {
    void updateUserIsFixedByMeeting(final Meeting meeting, final List<Long> users);

    List<UserVo> findByAvailableDateAndTimeSlots(Long availableDateId, List<TimeSlot> timeSlots);
}
