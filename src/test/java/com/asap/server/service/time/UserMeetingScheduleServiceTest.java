package com.asap.server.service.time;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.time.vo.TimeBlockVo;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMeetingScheduleServiceTest {
    @Mock
    private UserMeetingScheduleRepository userMeetingScheduleRepository;
    @InjectMocks
    private UserMeetingScheduleService userMeetingScheduleService;

    @Test
    @DisplayName("input 6:00 - 08:00 , return 06:00 - 07:30")
    void test() {
        // given
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule
                .builder()
                .userId(1L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_6_00)
                .endTimeSlot(TimeSlot.SLOT_8_00)
                .build();

        UserMeetingSchedule userMeetingSchedule2 = UserMeetingSchedule
                .builder()
                .userId(2L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_6_00)
                .endTimeSlot(TimeSlot.SLOT_8_00)
                .build();

        List<UserMeetingSchedule> userMeetingSchedules = List.of(userMeetingSchedule, userMeetingSchedule2);
        when(userMeetingScheduleRepository.findAllByMeetingId(1L)).thenReturn(userMeetingSchedules);

        List<TimeBlockVo> expected = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_6_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_6_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_7_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_7_30, 0, List.of(1L, 2L))
        );

        // when
        List<TimeBlockVo> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("input empty list , return empty list")
    void test2() {
        // given
        when(userMeetingScheduleRepository.findAllByMeetingId(1L)).thenReturn(Collections.emptyList());

        // when
        List<TimeBlockVo> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("input [06:00 - 07:00, 가중치: 1], [06:00 - 08:00, 가중치: 2], return [06:00 - 06:30 가중치 3], [07:00 - 07:30 가중치 2]")
    void test3() {
        // given
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule
                .builder()
                .userId(1L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_6_00)
                .endTimeSlot(TimeSlot.SLOT_7_00)
                .weight(1)
                .build();

        UserMeetingSchedule userMeetingSchedule2 = UserMeetingSchedule
                .builder()
                .userId(2L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_6_00)
                .endTimeSlot(TimeSlot.SLOT_8_00)
                .weight(2)
                .build();

        List<UserMeetingSchedule> userMeetingSchedules = List.of(userMeetingSchedule, userMeetingSchedule2);
        when(userMeetingScheduleRepository.findAllByMeetingId(1L)).thenReturn(userMeetingSchedules);

        List<TimeBlockVo> expected = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_6_00, 3, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_6_30, 3, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_7_00, 2, List.of(2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_7_30, 2, List.of(2L))
        );

        // when
        List<TimeBlockVo> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response).isEqualTo(expected);
    }

    @Test
    @DisplayName("input [유저 1: 10:00 - 13:30], [유저 2: 08:30 - 11:00, 11:00 - 13:00], return [08:30 - 09:30 유저 2], [10:00 - 12:30 유저 1,2], [13:00, 유저 1]")
    void test4() {
        // given
        UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule
                .builder()
                .userId(1L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_10_00)
                .endTimeSlot(TimeSlot.SLOT_13_30)
                .weight(0)
                .build();

        UserMeetingSchedule userMeetingSchedule2 = UserMeetingSchedule
                .builder()
                .userId(2L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_8_30)
                .endTimeSlot(TimeSlot.SLOT_11_00)
                .weight(0)
                .build();
        UserMeetingSchedule userMeetingSchedule3 = UserMeetingSchedule
                .builder()
                .userId(2L)
                .availableDate(LocalDate.of(2024, 7, 9))
                .startTimeSlot(TimeSlot.SLOT_11_00)
                .endTimeSlot(TimeSlot.SLOT_13_00)
                .weight(0)
                .build();

        List<UserMeetingSchedule> userMeetingSchedules =
                List.of(userMeetingSchedule, userMeetingSchedule2, userMeetingSchedule3);
        when(userMeetingScheduleRepository.findAllByMeetingId(1L)).thenReturn(userMeetingSchedules);

        List<TimeBlockVo> expected = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_8_30, 0, List.of(2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_9_00, 0, List.of(2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_9_30, 0, List.of(2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_10_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_10_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_11_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_11_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_13_00, 0, List.of(1L))
        );

        // when
        List<TimeBlockVo> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response).isEqualTo(expected);
    }
}