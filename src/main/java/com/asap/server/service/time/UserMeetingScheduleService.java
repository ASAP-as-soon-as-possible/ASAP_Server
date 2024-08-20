package com.asap.server.service.time;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.time.dto.UserMeetingScheduleRegisterDto;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.time.vo.UserScheduleByTimeSlot;
import com.asap.server.service.time.vo.UserScheduleByTimeSlot.CompositeKey;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .build();
        userMeetingScheduleRepository.save(userMeetingSchedule);
    }

    @Transactional(readOnly = true)
    public List<TimeBlockVo> getTimeBlocks(final Long meetingId) {
        List<UserMeetingSchedule> userMeetingSchedules = userMeetingScheduleRepository.findAllByMeetingId(meetingId);

        return userMeetingSchedules.stream()
                .flatMap(this::convertToUserScheduleByTimeSlot)
                .collect(Collectors.groupingBy(UserScheduleByTimeSlot::composeKey))
                .entrySet().stream()
                .map(this::convertToTimeBlock)
                .sorted()
                .toList();
    }

    private Stream<UserScheduleByTimeSlot> convertToUserScheduleByTimeSlot(
            final UserMeetingSchedule userMeetingSchedule
    ) {
        return TimeSlot.getTimeSlots(
                        userMeetingSchedule.getStartTimeSlot().getIndex(),
                        userMeetingSchedule.getEndTimeSlot().getIndex()
                )
                .stream()
                .map(timeSlot -> new UserScheduleByTimeSlot(
                                userMeetingSchedule.getId(),
                                userMeetingSchedule.getAvailableDate(),
                                userMeetingSchedule.getUserId(),
                                timeSlot,
                                userMeetingSchedule.getWeight()
                        )
                );
    }

    private TimeBlockVo convertToTimeBlock(
            final Entry<CompositeKey, List<UserScheduleByTimeSlot>> entry
    ) {
        List<Long> users = entry.getValue().stream()
                .map(UserScheduleByTimeSlot::userId)
                .toList();

        int weight = entry.getValue().stream()
                .mapToInt(UserScheduleByTimeSlot::weight)
                .sum();

        return new TimeBlockVo(entry.getKey().availableDate(), entry.getKey().time(), weight, users);
    }
}
