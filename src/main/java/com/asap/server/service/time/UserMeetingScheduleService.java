package com.asap.server.service.time;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.time.dto.register.UserMeetingScheduleRegisterDto;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.time.vo.UserScheduleByTimeSlotVo;
import com.asap.server.service.time.vo.UserScheduleByTimeSlotVo.CompositeKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            final long meetingId,
            final long userId,
            final List<UserMeetingScheduleRegisterDto> availableDates
    ) {
        isDuplicatedDate(availableDates);

        availableDates.forEach(availableDate -> {
                    UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule.builder()
                            .userId(userId)
                            .meetingId(meetingId)
                            .availableDate(DateUtil.transformLocalDate(availableDate.month(), availableDate.day()))
                            .startTimeSlot(availableDate.startTime())
                            .endTimeSlot(availableDate.endTime())
                            .weight(availableDate.priority())
                            .build();

                    userMeetingScheduleRepository.save(userMeetingSchedule);
                }
        );
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

    public boolean isEmptyHostTimeBlock(final long hostId) {
        return userMeetingScheduleRepository.countAllByUserId(hostId) == 0;
    }

    private void isDuplicatedDate(final List<UserMeetingScheduleRegisterDto> registerDto) {
        Map<String, List<TimeSlot>> meetingTimeAvailable = new HashMap<>();
        for (UserMeetingScheduleRegisterDto requestDto : registerDto) {
            String col = String.format("%s %s", requestDto.month(), requestDto.day());
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(requestDto.startTime().ordinal(),
                    requestDto.endTime().ordinal() - 1);
            if (meetingTimeAvailable.containsKey(col)) {
                if (meetingTimeAvailable.get(col).stream().anyMatch(timeSlots::contains)) {
                    throw new BadRequestException(Error.DUPLICATED_TIME_EXCEPTION);
                }
            } else {
                meetingTimeAvailable.put(col, timeSlots);
            }
        }
    }

    private Stream<UserScheduleByTimeSlotVo> convertToUserScheduleByTimeSlot(
            final UserMeetingSchedule userMeetingSchedule
    ) {
        return TimeSlot.getTimeSlots(
                        userMeetingSchedule.getStartTimeSlot().getIndex(),
                        userMeetingSchedule.getEndTimeSlot().getIndex() - 1
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
