package com.asap.server.service.time;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.asap.server.common.generator.TimeBlockDtoGenerator;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.repository.UserMeetingScheduleRepository;
import com.asap.server.service.time.vo.TimeBlock;
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
    @DisplayName("시간별로 사용자를 구할 수 있다.")
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

        List<TimeBlock> expected = TimeBlockDtoGenerator.generator(
                LocalDate.of(2024, 7, 9),
                TimeSlot.SLOT_6_00,
                TimeSlot.SLOT_8_00,
                0,
                List.of(1L, 2L)
        );

        // when
        List<TimeBlock> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("빈 리스트를 반환할 때 시간 블록이 비어있는지 확인한다.")
    void test2() {
        // given
        when(userMeetingScheduleRepository.findAllByMeetingId(1L)).thenReturn(Collections.emptyList());

        // when
        List<TimeBlock> response = userMeetingScheduleService.getTimeBlocks(1L);

        // then
        assertThat(response.isEmpty()).isTrue();
    }
}