package com.asap.server.service.time;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.time.dto.register.UserMeetingScheduleRegisterDto;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.time.vo.UserScheduleByTimeSlotVo;
import com.asap.server.service.time.vo.UserScheduleByTimeSlotVo.CompositeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserMeetingScheduleService {
    private final UserMeetingScheduleRepository userMeetingScheduleRepository;

    @Transactional
    public void createUserMeetingSchedule(
            final Long userId,
            final Long meetingId,
            final UserMeetingScheduleRegisterDto registerDto
    ) {
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule.builder()
                .userId(userId)
                .meetingId(meetingId)
                .availableDate(DateUtil.transformLocalDate(registerDto.month(), registerDto.day()))
                .startTimeSlot(registerDto.startTime())
                .endTimeSlot(registerDto.endTime())
                .weight(registerDto.priority())
                .build();
        userMeetingScheduleRepository.save(userMeetingSchedule);
    }

    @Transactional(readOnly = true)
    public List<TimeBlockVo> getTimeBlocks(final Long meetingId) {
        List<UserMeetingSchedule> userMeetingSchedules = userMeetingScheduleRepository.findAllByMeetingId(meetingId);

        return userMeetingSchedules.stream()
                .flatMap(this::convertToUserScheduleByTimeSlot)
                .collect(Collectors.groupingBy(UserScheduleByTimeSlotVo::composeKey))
                .entrySet().stream()
                .map(this::convertToTimeBlock)
                .sorted()
                .toList();
    }

    private Stream<UserScheduleByTimeSlotVo> convertToUserScheduleByTimeSlot(
            final UserMeetingSchedule userMeetingSchedule
    ) {
        return TimeSlot.getTimeSlots(
                        userMeetingSchedule.getStartTimeSlot().getIndex(),
                        userMeetingSchedule.getEndTimeSlot().getIndex()
                )
                .stream()
                .map(timeSlot -> new UserScheduleByTimeSlotVo(
                                userMeetingSchedule.getId(),
                                userMeetingSchedule.getAvailableDate(),
                                userMeetingSchedule.getUserId(),
                                timeSlot,
                                userMeetingSchedule.getWeight()
                        )
                );
    }

    private TimeBlockVo convertToTimeBlock(
            final Entry<CompositeKey, List<UserScheduleByTimeSlotVo>> entry
    ) {
        List<Long> userIds = entry.getValue().stream()
                .map(UserScheduleByTimeSlotVo::userId)
                .toList();

        int weight = entry.getValue().stream()
                .mapToInt(UserScheduleByTimeSlotVo::weight)
                .sum();

        return new TimeBlockVo(entry.getKey().availableDate(), entry.getKey().time(), weight, userIds);
    }
}
