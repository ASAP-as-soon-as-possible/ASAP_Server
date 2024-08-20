package com.asap.server.service.time;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.dto.UserMeetingScheduleRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMeetingScheduleService {
    private final UserMeetingScheduleRepository userMeetingScheduleRepository;

    @Transactional
    public void createUserMeetingSchedule(final Long userId, final Long meetingId, final UserMeetingScheduleRegisterDto registerDto) {
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule.builder()
                .userId(userId)
                .meetingId(meetingId)
                .availableDate(DateUtil.transformLocalDate(registerDto.month(), registerDto.day()))
                .startTimeSlot(registerDto.startTime())
                .endTimeSlot(registerDto.endTime())
                .build();
        userMeetingScheduleRepository.save(userMeetingSchedule);
    }
}
