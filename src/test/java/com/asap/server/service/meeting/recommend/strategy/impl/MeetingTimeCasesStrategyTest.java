package com.asap.server.service.meeting.recommend.strategy.impl;


import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.asap.server.persistence.domain.enums.Duration.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MeetingTimeCasesStrategyTest {
    private MeetingTimeCasesStrategyImpl strategy;

    @BeforeEach
    void setUp() {
        strategy = new MeetingTimeCasesStrategyImpl();
    }


    @Nested
    @DisplayName("30분 회의일 때")
    class HalfDurationTest {
        Duration duration = HALF;

        @Test
        @DisplayName("한 명이 30분 회의를 진행할 경우, 1개의 케이스를 반환한다.")
        void findOneMemberTest() {
            int userCount = 1;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HALF, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("두 명이 30분 회의를 진행할 경우, 1개의 케이스를 반환한다.")
        void findTwoMemberTest() {
            int userCount = 2;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


        @Test
        @DisplayName("일곱 명이 30분 회의를 진행할 경우, 6개의 케이스를 반환한다.")
        void findSevenMemberTest() {
            int userCount = 7;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("열 명이 30분 회의를 진행할 경우, 10개의 케이스를 반환한다.")
        void findTestMemberTest() {
            int userCount = 10;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HALF, 10),
                    new PossibleTimeCaseVo(HALF, 9),
                    new PossibleTimeCaseVo(HALF, 8),
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }


    @Nested
    @DisplayName("1시간 회의")
    class OneHourDurationTest {
        Duration duration = HOUR;


        @Test
        @DisplayName("한 명이 1시간 회의를 진행할 경우, 2개의 케이스를 반환한다.")
        void findOneMemberOneHour() {
            int userCount = 1;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 1),
                    new PossibleTimeCaseVo(HALF, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


        @Test
        @DisplayName("두 명이 1시간 회의를 진행할 경우, 2개의 케이스를 반환한다.")
        void findTwoMemberOneHour() {
            int userCount = 2;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);

        }

        @Test
        @DisplayName("일곱 명이 1시간 회의를 진행할 경우, 12개의 케이스를 반환한다.")
        void findSevenMemberTest() {
            int userCount = 7;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("열 명이 1시간 회의를 진행할 경우, 18개의 케이스를 반환한다.")
        void findTenMemberTest() {
            int userCount = 10;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 10),
                    new PossibleTimeCaseVo(HALF, 10),
                    new PossibleTimeCaseVo(HOUR, 9),
                    new PossibleTimeCaseVo(HALF, 9),
                    new PossibleTimeCaseVo(HOUR, 8),
                    new PossibleTimeCaseVo(HALF, 8),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("1시간 30분 회의")
    class HourHalfTest {
        Duration duration = HOUR_HALF;

        @DisplayName("한 명이 1시간 30분 회의를 진행할 경우, 3개의 케이스를 반환한다.")
        @Test
        void findOneMemberTest() {
            int userCount = 1;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR_HALF, 1),
                    new PossibleTimeCaseVo(HOUR, 1),
                    new PossibleTimeCaseVo(HALF, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 1시간 30분 회의를 진행할 경우, 3개의 케이스를 반환한다.")
        @Test
        void findTwoMemberTest() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("일곱 명이 1시간 30분 회의를 진행할 경우, 18개의 케이스를 반환한다.")
        @Test
        void findSevenMemberTest() {
            int userCount = 7;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("10명이 1시간 30분 회의를 진행할 경우 27개의 리스트를 반환한다.")
        @Test
        void findTenMemberTest() {
            int userCount = 10;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR_HALF, 10),
                    new PossibleTimeCaseVo(HOUR, 10),
                    new PossibleTimeCaseVo(HOUR_HALF, 9),
                    new PossibleTimeCaseVo(HOUR, 9),
                    new PossibleTimeCaseVo(HOUR_HALF, 8),
                    new PossibleTimeCaseVo(HOUR, 8),
                    new PossibleTimeCaseVo(HALF, 10),
                    new PossibleTimeCaseVo(HALF, 9),
                    new PossibleTimeCaseVo(HALF, 8),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(HALF, 7),
                    new PossibleTimeCaseVo(HALF, 6),
                    new PossibleTimeCaseVo(HALF, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 4),
                    new PossibleTimeCaseVo(HALF, 3),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            // when
            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


    }

    @Nested
    @DisplayName("2시간 회의")
    class TwoHourTest {

        Duration duration = TWO_HOUR;

        @DisplayName("한 명이 2시간 회의를 진행할 경우, 3개의 케이스를 반환한다.")
        @Test
        void findOneMemberTest() {
            int userCount = 1;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR, 1),
                    new PossibleTimeCaseVo(HOUR_HALF, 1),
                    new PossibleTimeCaseVo(HOUR, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 2시간 회의를 진행할 경우, 3개의 케이스를 반환한다.")
        @Test
        void findTwoMemberTest() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("일곱 명이 2시간 회의를 진행할 경우, 24개의 케이스를 반환한다.")
        @Test
        void findSevenMemberTest() {
            int userCount = 7;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("10명이 2시간 회의를 진행할 경우 27개의 리스트를 반환한다.")
        @Test
        void findTenMemberTest() {
            int userCount = 10;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR, 10),
                    new PossibleTimeCaseVo(HOUR_HALF, 10),
                    new PossibleTimeCaseVo(TWO_HOUR, 9),
                    new PossibleTimeCaseVo(HOUR_HALF, 9),
                    new PossibleTimeCaseVo(TWO_HOUR, 8),
                    new PossibleTimeCaseVo(HOUR_HALF, 8),
                    new PossibleTimeCaseVo(HOUR, 10),
                    new PossibleTimeCaseVo(HOUR, 9),
                    new PossibleTimeCaseVo(HOUR, 8),
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(HOUR, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 4),
                    new PossibleTimeCaseVo(HOUR, 3),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            // when
            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

    }

    @Nested
    @DisplayName("2시간 30분 회의")
    class TwoHourHalfTest {
        Duration duration = TWO_HOUR_HALF;

        @DisplayName("한 명이 2시간 회의를 진행할 경우, 4개의 케이스를 반환한다.")
        @Test
        void findOneMemberTest() {
            int userCount = 1;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 1),
                    new PossibleTimeCaseVo(TWO_HOUR, 1),
                    new PossibleTimeCaseVo(HOUR_HALF, 1),
                    new PossibleTimeCaseVo(HOUR, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 2시간 회의를 진행할 경우, 4개의 케이스를 반환한다.")
        @Test
        void findTwoMemberTest() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("일곱 명이 2시간 30분 회의를 진행할 경우, 18개의 케이스를 반환한다.")
        @Test
        void findSevenMemberTest() {
            int userCount = 7;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("10명이 2시간 30분 회의를 진행할 경우 27개의 리스트를 반환한다.")
        @Test
        void findTenMemberTest() {
            int userCount = 10;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 10),
                    new PossibleTimeCaseVo(TWO_HOUR, 10),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 9),
                    new PossibleTimeCaseVo(TWO_HOUR, 9),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 8),
                    new PossibleTimeCaseVo(TWO_HOUR, 8),
                    new PossibleTimeCaseVo(HOUR_HALF, 10),
                    new PossibleTimeCaseVo(HOUR, 10),
                    new PossibleTimeCaseVo(HOUR_HALF, 9),
                    new PossibleTimeCaseVo(HOUR, 9),
                    new PossibleTimeCaseVo(HOUR_HALF, 8),
                    new PossibleTimeCaseVo(HOUR, 8),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 5),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 4),
                    new PossibleTimeCaseVo(HOUR_HALF, 3),
                    new PossibleTimeCaseVo(HOUR_HALF, 2)
            );

            // when
            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("3시간 회의")
    class ThreeHourTest {
        Duration duration = THREE_HOUR;

        @Test
        @DisplayName("한 명이 3시간 회의를 진행할 경우, 5개의 케이스를 반환한다.")
        void findOneMemberTest() {
            int userCount = 1;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR, 1),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 1),
                    new PossibleTimeCaseVo(TWO_HOUR, 1),
                    new PossibleTimeCaseVo(HOUR_HALF, 1),
                    new PossibleTimeCaseVo(HOUR, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 3시간 회의를 진행할 경우, 5개의 케이스를 반환한다.")
        @Test
        void findTwoMemberTest() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR, 2),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("7 명이 3시간 회의를 진행할 경우, 22개의 케이스를 반환한다.")
        void findSevenMemberThreeHour() {
            int userCount = 7;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR, 7),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 7),
                    new PossibleTimeCaseVo(THREE_HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(HOUR_HALF, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(HOUR_HALF, 6),
                    new PossibleTimeCaseVo(HOUR, 7),
                    new PossibleTimeCaseVo(HOUR, 6),
                    new PossibleTimeCaseVo(THREE_HOUR, 5),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 5),
                    new PossibleTimeCaseVo(THREE_HOUR, 4),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(THREE_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 3),
                    new PossibleTimeCaseVo(THREE_HOUR, 2),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


        @DisplayName("10명이 3시간 회의를 진행할 경우, 32개의 케이스를 반환한다.")
        @Test
        void findTenMemberTest() {
            int userCount = 10;

            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR, 10),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 10),
                    new PossibleTimeCaseVo(THREE_HOUR, 9),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 9),
                    new PossibleTimeCaseVo(THREE_HOUR, 8),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 8),
                    new PossibleTimeCaseVo(TWO_HOUR, 10),
                    new PossibleTimeCaseVo(HOUR_HALF, 10),
                    new PossibleTimeCaseVo(TWO_HOUR, 9),
                    new PossibleTimeCaseVo(HOUR_HALF, 9),
                    new PossibleTimeCaseVo(TWO_HOUR, 8),
                    new PossibleTimeCaseVo(HOUR_HALF, 8),
                    new PossibleTimeCaseVo(HOUR, 10),
                    new PossibleTimeCaseVo(HOUR, 9),
                    new PossibleTimeCaseVo(HOUR, 8),
                    new PossibleTimeCaseVo(THREE_HOUR, 7),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 7),
                    new PossibleTimeCaseVo(THREE_HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 6),
                    new PossibleTimeCaseVo(THREE_HOUR, 5),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 5),
                    new PossibleTimeCaseVo(TWO_HOUR, 7),
                    new PossibleTimeCaseVo(TWO_HOUR, 6),
                    new PossibleTimeCaseVo(TWO_HOUR, 5),
                    new PossibleTimeCaseVo(THREE_HOUR, 4),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 4),
                    new PossibleTimeCaseVo(THREE_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 3),
                    new PossibleTimeCaseVo(THREE_HOUR, 2),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 4),
                    new PossibleTimeCaseVo(TWO_HOUR, 3),
                    new PossibleTimeCaseVo(TWO_HOUR, 2)
            );
            List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }

}
