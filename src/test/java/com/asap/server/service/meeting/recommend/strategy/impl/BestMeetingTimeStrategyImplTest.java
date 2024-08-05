package com.asap.server.service.meeting.recommend.strategy.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.meeting.recommend.strategy.BestMeetingTimeStrategy;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BestMeetingTimeStrategyImplTest {
    private final BestMeetingTimeStrategy continuousMeetingTimeStrategy = new BestMeetingTimeStrategyImpl();

    @DisplayName("회의 진행 시간이 30분인 경우, 3시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationHalfTest {
        Duration duration = Duration.HALF;

        @DisplayName("14:00 - 16:00 returns 14:00 - 14:30")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_14_30, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("14:00 - 17:00 returns 14:00 - 14:30, 16:30 - 17:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_17_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_14_30, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_16_30, TimeSlot.SLOT_17_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("6:00 - 6:30, 21:00 - 24:00 returns 6:00 - 6:30, 21:00 - 21:30, 23:30 - 24:00")
        @Test
        void test3() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_6_00, TimeSlot.SLOT_6_30, 0);
            BestMeetingTimeVo bestMeetingTimeVo2 =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_21_00, TimeSlot.SLOT_24_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo, bestMeetingTimeVo2);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_6_00, TimeSlot.SLOT_6_30, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_21_00, TimeSlot.SLOT_21_30, 0);
            BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_23_30, TimeSlot.SLOT_24_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2, e3);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }

    @DisplayName("회의 진행 시간이 1시간인 경우, 4시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationHourTest {
        Duration duration = Duration.HOUR;

        @DisplayName("14:00 - 16:00 returns 14:00 - 15:00")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_15_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("14:00 - 18:00 returns 14:00 - 15:00, 17:00 - 18:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_18_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_15_00, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_17_00, TimeSlot.SLOT_18_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }

    @DisplayName("회의 진행 시간이 1시간 30분인 경우, 5시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationHourHalfTest {
        Duration duration = Duration.HOUR_HALF;

        @DisplayName("14:00 - 16:00 returns 14:00 - 15:30")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_30, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_15_30, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("14:00 - 19:00 returns 14:00 - 15:30, 17:30 - 19:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_19_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_15_30, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_17_30, TimeSlot.SLOT_19_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }

    @DisplayName("회의 진행 시간이 2시간인 경우, 6시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationTwoHourTest {
        Duration duration = Duration.TWO_HOUR;

        @DisplayName("14:00 - 16:00 returns 14:00 - 16:00")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }


        @DisplayName("14:00 - 20:00 returns 14:00 - 16:00, 18:00 - 20:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_20_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }

    @DisplayName("회의 진행 시간이 2시간 30분인 경우, 7시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationTwoHourHalfTest {
        Duration duration = Duration.TWO_HOUR_HALF;

        @DisplayName("14:00 - 17:00 returns 14:00 - 16:30")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_17_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_30, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("14:00 - 21:00 returns 14:00 - 16:30, 19:30 - 21:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_21_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_30, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_18_30, TimeSlot.SLOT_21_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }

    @DisplayName("회의 진행 시간이 3시간인 경우, 8시간 이상인 블록은 긴 블록으로 처리한다.")
    @Nested
    class DurationThreeHourTest {
        Duration duration = Duration.THREE_HOUR;

        @DisplayName("14:00 - 17:00 returns 14:00 - 17:00")
        @Test
        void test() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_17_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_17_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }

        @DisplayName("14:00 - 22:00 returns 14:00 - 17:00, 19:00 - 22:00")
        @Test
        void test2() {
            // given
            LocalDate availableDate = LocalDate.of(2023, 7, 10);
            BestMeetingTimeVo bestMeetingTimeVo =
                    new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_22_00, 0);
            List<BestMeetingTimeVo> candidateMeetingTimes = List.of(bestMeetingTimeVo);

            BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_14_00, TimeSlot.SLOT_17_00, 0);
            BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, TimeSlot.SLOT_19_00, TimeSlot.SLOT_22_00, 0);
            List<BestMeetingTimeVo> expected = List.of(e1, e2);

            // when
            List<BestMeetingTimeVo> response = continuousMeetingTimeStrategy.find(candidateMeetingTimes, duration);

            // then
            assertThat(response).isEqualTo(expected);
        }
    }
}