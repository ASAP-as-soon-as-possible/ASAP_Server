package com.asap.server.service;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.presentation.controller.dto.request.UserMeetingTimeSaveRequestDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMeetingScheduleService {
    private final UserMeetingScheduleRepository userMeetingScheduleRepository;

    @Transactional
    public void createUserMeetingSchedule(final Long userId, final Long meetingId, final UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto) {
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule.builder()
                .userId(userId).meetingId(meetingId)
                .availableDate(DateUtil.transformLocalDate(userMeetingTimeSaveRequestDto.month(), userMeetingTimeSaveRequestDto.day()))
                .startTimeSlot(userMeetingTimeSaveRequestDto.startTime())
                .endTimeSlot(userMeetingTimeSaveRequestDto.endTime())
                .build();
        userMeetingScheduleRepository.save(userMeetingSchedule);
    }
}
