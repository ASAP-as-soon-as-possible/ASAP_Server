package com.asap.server.service.meeting.recommend.strategy.impl;


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
    class TenMemberTest {

        @DisplayName("10명이 1시간 30분 회의를 진행할 경우 27개의 리스트를 반환한다.")
        @Test
        void findTimeCasesOverStandardRatio() {
            // given
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
            List<PossibleTimeCaseVo> result = strategy.find(HOUR_HALF, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


        @DisplayName("10명이 3시간 회의를 진행할 경우, 32개의 케이스를 반환한다.")
        @Test
        void findPossibleTimeForTenMemberThreeHour() {
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
            List<PossibleTimeCaseVo> result = strategy.find(THREE_HOUR, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }

    @Nested
    class OneMemberTest {
        int userCount = 1;
        @Test
        @DisplayName("한명이 3시간 회의를 입력했을 때")
        void findOneMemberThreeHour() {
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR,1),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 1),
                    new PossibleTimeCaseVo(TWO_HOUR, 1),
                    new PossibleTimeCaseVo(HOUR_HALF, 1),
                    new PossibleTimeCaseVo(HOUR, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(THREE_HOUR, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("한명이 1시간 회의를 입력했을 때")
        void findOneMemberOneHour() {
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 1),
                    new PossibleTimeCaseVo(HALF, 1)
            );

            List<PossibleTimeCaseVo> result = strategy.find(HOUR, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }
    }

    @Nested
    class TwoMemberTest {
        int userCount = 2;

        @Test
        @DisplayName("두 명이 1시간 회의를 진행할 경우")
        void findTwoMemberOneHour() {
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(HOUR, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 3시간 회의를 진행할 경우")
        @Test
        void findPossibleTimeForTwoMemberOneThreeHour() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(THREE_HOUR, 2),
                    new PossibleTimeCaseVo(TWO_HOUR_HALF, 2),
                    new PossibleTimeCaseVo(TWO_HOUR, 2),
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(THREE_HOUR, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }

        @DisplayName("두 명이 1시간 30분 회의를 진행할 경우")
        @Test
        void findPossibleTimeForTwoMemberOneHourHalf() {
            int userCount = 2;
            List<PossibleTimeCaseVo> response = List.of(
                    new PossibleTimeCaseVo(HOUR_HALF, 2),
                    new PossibleTimeCaseVo(HOUR, 2),
                    new PossibleTimeCaseVo(HALF, 2)
            );

            List<PossibleTimeCaseVo> result = strategy.find(HOUR_HALF, userCount);
            assertThat(result.size()).isEqualTo(response.size());
            assertThat(result).isEqualTo(response);
        }


    }
}
