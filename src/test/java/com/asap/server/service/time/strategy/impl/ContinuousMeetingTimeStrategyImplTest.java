package com.asap.server.service.time.strategy.impl;

import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_14_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_14_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_16_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_16_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_17_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_18_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_20_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_23_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_24_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_6_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_6_30;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.time.strategy.ContinuousMeetingTimeStrategy;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.time.vo.BestMeetingTimeVo;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ContinuousMeetingTimeStrategyImplTest {
    private final ContinuousMeetingTimeStrategy continuousMeetingTimeStrategy = new ContinuousMeetingTimeStrategyImpl();

    @DisplayName("회의 진행 시간보다 긴 시간대인 12시부터 16시까지 30분 간격의 TimeBlocks과")
    @Nested
    class ReturnOneBestMeetingTimeVoByDuration {
        // given
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_30, 0, List.of(1L, 2L))
        );
        BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_16_00, 0, List.of(1L, 2L));
        List<BestMeetingTimeVo> result = List.of(r1);

        @DisplayName("회의 진행 시간이 주어졌을 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"HALF", "HOUR", "HOUR_HALF", "TWO_HOUR", "TWO_HOUR_HALF",
                "THREE_HOUR"})
        void durationsTest(Duration duration) {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }
    }

    @DisplayName("12시부터 16시, 18시부터 20시까지 30분 간격의 timeBlocks이 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration {
        // given
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_18_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_18_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_19_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_19_30, 0, List.of(1L, 2L))
        );

        @DisplayName("회의 진행 시간이 2시간 이하일 때, 12시부터 16시까지, 18시부터 20시 까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"HALF", "HOUR", "HOUR_HALF", "TWO_HOUR"})
        void durationsTest2(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_16_00, 0, List.of(1L, 2L));
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_18_00, SLOT_20_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분이상일 때, 12시부터 16시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"TWO_HOUR_HALF", "THREE_HOUR"})
        void durationsTest3(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_16_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }
    }

    @DisplayName("12시부터 12시 30분까지 30분 간격인 timeBlocks이 있고")
    @Nested
    class ReturnOneBestMeetingTimeVoByDuration2 {
        // given
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L))
        );

        @DisplayName("회의 진행 시간이 30분일 때, 12시부터 12시 30분까지인 BestMeetingTimeVo를 반환한다.")
        @Test
        void durationHalfTest() {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_12_30, 0, List.of(1L, 2L));
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

    @DisplayName("2023-7-10일 12시부터 14시, 2023-7-11일 12시부터 13시까지인 timeBlocks이 30분 간격으로 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration2 {
        // given
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L))
        );

        @DisplayName("회의 진행 시간이 1시간 이하일 때, 12시부터 14시, 12시부터 13시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"HALF", "HOUR"})
        void durationsTest(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_14_00, 0, List.of(1L, 2L));
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 11), SLOT_12_00, SLOT_13_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 이상 2시간 30분 미만일 때, 12시부터 14시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"HOUR_HALF", "TWO_HOUR"})
        void durationsTest2(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_14_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분 이상일 때, 빈 리스트를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"TWO_HOUR_HALF", "THREE_HOUR"})
        void durationsTest3(Duration duration) {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response.isEmpty()).isTrue();
        }
    }

    @DisplayName("2023-7-10일 12시부터 14시, 2023-7-11일 14시30분부터 17시까지 30분 간격인 timeBlocks이 있고")
    @Nested
    class ReturnBestMeetingTimeVoByDuration3 {
        // given
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_15_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_16_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_16_30, 0, List.of(1L, 2L))
        );

        @DisplayName("회의 진행 시간이 2시간 이하일 때, 2023-7-10일 12시부터 14시, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"HALF", "HOUR", "HOUR_HALF", "TWO_HOUR"})
        void durationsTest(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_14_00, 0, List.of(1L, 2L));
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 11), SLOT_14_30, SLOT_17_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 2시간 30분 이상 3시간 미만일 때, 2023-7-11일 14시 30분부터 17시까지인 BestMeetingTimeVo를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"TWO_HOUR_HALF"})
        void durationTwoHourHalfTest(Duration duration) {
            // given
            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 11), SLOT_14_30, SLOT_17_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("회의 진행 시간이 3시간일 때, 빈 리스트를 반환한다.")
        @ParameterizedTest
        @EnumSource(value = Duration.class, names = {"THREE_HOUR"})
        void durationThreeHourTest(Duration duration) {
            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, duration);

            // then
            assertThat(response.isEmpty()).isTrue();
        }
    }

    @DisplayName("가중치 테스트")
    @Nested
    class SumWeightTest {
        @DisplayName("[(연속된 timeBlock의 가중치의 총 합) % (연속된 timeBlock의 수)]를 weight로 넣는다.")
        @Test
        void weightTest() {
            // given
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 4, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 4, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 4, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 4, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_16_00, 2, List.of(1L, 2L))
            );

            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_14_00, 4, List.of(1L, 2L));
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 11), SLOT_16_00, SLOT_16_30, 2, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r1, r2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }

        @DisplayName("가중치가 큰 BestMeetingVo 순으로 반환된다.")
        @Test
        void weightTest2() {
            // given
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 2, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 2, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 2, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 2, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_16_00, 4, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 12), TimeSlot.SLOT_16_30, 1, List.of(1L, 2L))
            );

            BestMeetingTimeVo r1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_12_00, SLOT_14_00, 2, List.of(1L, 2L));
            BestMeetingTimeVo r2 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 11), SLOT_16_00, SLOT_16_30, 4, List.of(1L, 2L));
            BestMeetingTimeVo r3 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 12), SLOT_16_30, SLOT_17_00, 1, List.of(1L, 2L));
            List<BestMeetingTimeVo> result = List.of(r2, r1, r3);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(result);
        }
    }

    @DisplayName("TimeSlot 양 끝단(06_00, 24_00) 테스트")
    @Nested
    class TimeSlotEdgeTest {
        @DisplayName("회의 진행 시간이 30분이고 12시부터 12시 30분까지 30분 간격인 timeBlocks이 있을 때, 06:00 ~ 06:30을 반환한다.")
        @Test
        void timeSlot6_00Test() {
            // given
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_6_00, 0, List.of(1L, 2L))
            );

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_6_00, SLOT_6_30, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("회의 진행 시간이 30분이고 23시 30분부터 24시까지 30분 간격인 timeBlocks이 있을 때, 23:30 ~ 24:00을 반환한다.")
        @Test
        void timeSlot24_00Test() {
            // given
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_23_30, 0, List.of(1L, 2L))
            );

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(LocalDate.of(2024, 7, 10), SLOT_23_30, SLOT_24_00, 0, List.of(1L, 2L));
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(timeBlocks, Duration.HALF);

            // then
            assertThat(response).isEqualTo(expected);
        }

    }
}
