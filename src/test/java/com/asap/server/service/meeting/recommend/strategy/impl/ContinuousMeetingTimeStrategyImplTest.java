package com.asap.server.service.meeting.recommend.strategy.impl;

import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_16_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_18_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_20_00;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.common.generator.TimeBlockDtoGenerator;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.recommend.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContinuousMeetingTimeStrategyImplTest {
    private final ContinuousMeetingTimeStrategy continuousMeetingTimeStrategy = new ContinuousMeetingTimeStrategyImpl();

    @DisplayName("timeBlocks 내에 12시부터 16시까지 30분 간격으로 있고")
    @Nested
    class ReturnOneBestMeetingTimeVoByDuration {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        List<TimeBlockDto> timeBlocks = TimeBlockDtoGenerator.generator(availableDate, SLOT_12_00, SLOT_16_00, 0, 2L);

        BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
        List<BestMeetingTimeVo> result = List.of(r1);

        @DisplayName("회의 진행 시간이 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourHalfTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourHalfTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 3시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationThreeHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.THREE_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }
    }

}
