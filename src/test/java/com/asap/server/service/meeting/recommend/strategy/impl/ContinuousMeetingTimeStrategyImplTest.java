package com.asap.server.service.meeting.recommend.strategy.impl;

import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_14_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_14_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_16_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_17_00;
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

    @DisplayName("timeBlocks 내에 12시부터 16시, 18시부터 20시까지 30분 간격으로 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        List<TimeBlockDto> tempTimeBlocks = TimeBlockDtoGenerator.generator(availableDate, SLOT_12_00, SLOT_16_00, 0, 2L);
        List<TimeBlockDto> tempTimeBlocks2 = TimeBlockDtoGenerator.generator(availableDate, SLOT_18_00, SLOT_20_00, 0, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>() {{
            addAll(tempTimeBlocks);
            addAll(tempTimeBlocks2);
        }};

        @DisplayName("회의 진행 시간이 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate, SLOT_18_00, SLOT_20_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate, SLOT_18_00, SLOT_20_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate, SLOT_18_00, SLOT_20_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate, SLOT_18_00, SLOT_20_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 3시간일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationThreeHourTest() {
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_16_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.THREE_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }
    }

    @DisplayName("timeBlocks 내에 12시부터 12시 30분까지 30분 간격으로 있고")
    @Nested
    class ReturnOneBestMeetingTimeVoByDuration2 {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        List<TimeBlockDto> timeBlocks = TimeBlockDtoGenerator.generator(availableDate, SLOT_12_00, SLOT_12_30, 0, 2L);

        @DisplayName("회의 진행 시간이 30분일 때, 12시부터 12시 30분까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간일 때, 빈 리스트를 반환한다.")
        @Test
        void durationHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR);

            // then
            assertThat(response.size()).isZero();
        }
    }

    @DisplayName("timeBlocks 내에 2023-7-10일에는 12시부터 14시, 2023-7-11일에는 12시부터 13시까지 30분 간격으로 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration2 {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);
        List<TimeBlockDto> tempTimeBlocks = TimeBlockDtoGenerator.generator(availableDate, SLOT_12_00, SLOT_14_00, 0,
                2L);
        List<TimeBlockDto> tempTimeBlocks2 = TimeBlockDtoGenerator.generator(availableDate2, SLOT_12_00, SLOT_13_00, 0,
                2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>() {{
            addAll(tempTimeBlocks);
            addAll(tempTimeBlocks2);
        }};

        @DisplayName("회의 진행 시간이 30분일 때, 12시부터 14시, 12시부터 13시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_12_00, SLOT_13_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간일 때, 12시부터 14시, 12시부터 13시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_12_00, SLOT_13_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 1시간 30분일 때, 12시부터 14시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간일 때, 12시부터 14시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분일 때, 빈 리스트를 반환한다.")
        @Test
        void durationTwoHourHalfTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR_HALF);

            // then
            assertThat(response.isEmpty()).isTrue();
        }

        @DisplayName("회의 진행 시간이 3시간일 때, 빈 리스트를 반환한다.")
        @Test
        void durationThreeHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.THREE_HOUR);

            // then
            assertThat(response.isEmpty()).isTrue();
        }
    }

    @DisplayName("timeBlocks 내에 2023-7-10일에는 12시부터 14시, 2023-7-11일에는 14시30분부터 17시까지 30분 간격으로 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration3 {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);
        List<TimeBlockDto> tempTimeBlocks = TimeBlockDtoGenerator.generator(availableDate, SLOT_12_00, SLOT_14_00, 0,
                2L);
        List<TimeBlockDto> tempTimeBlocks2 = TimeBlockDtoGenerator.generator(availableDate2, SLOT_14_30, SLOT_17_00, 0,
                2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>() {{
            addAll(tempTimeBlocks);
            addAll(tempTimeBlocks2);
        }};

        @DisplayName("회의 진행 시간이 30분일 때, 2023-7-10일 12시부터 14시, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_14_30, SLOT_17_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 30분일 때, 2023-7-10일 12시부터 14시, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_14_30, SLOT_17_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 30분일 때, 2023-7-10일 12시부터 14시, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHourHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_14_30, SLOT_17_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 30분일 때, 2023-7-10일 12시부터 14시, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(availableDate2, SLOT_14_30, SLOT_17_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 30분일 때, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationTwoHourHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(availableDate2, SLOT_14_30, SLOT_17_00, 0);
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.TWO_HOUR_HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 3시간일 때, 빈 리스트를 반환한다.")
        @Test
        void durationThreeHourTest() {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.THREE_HOUR);

            // then
            assertThat(response.isEmpty()).isTrue();
        }
    }
}
