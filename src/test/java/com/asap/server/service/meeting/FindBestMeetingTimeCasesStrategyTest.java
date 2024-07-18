package com.asap.server.service.meeting;

import static com.asap.server.persistence.domain.enums.Duration.HALF;
import static com.asap.server.persistence.domain.enums.Duration.HOUR;
import static com.asap.server.persistence.domain.enums.Duration.HOUR_HALF;
import static com.asap.server.persistence.domain.enums.Duration.TWO_HOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindBestMeetingTimeCasesStrategyTest {
    private FindBestMeetingTimeCasesStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FindBestMeetingTimeCasesStrategy();
    }

    @DisplayName("회의 진행 시간이 2시간이고, 회의 참여 인원이 6명일 때 24개의 경우의 수가 나온다.")
    @Test
    void findOptimalMeetingTimeCasesTest() {
        // given
        Duration duration = TWO_HOUR;
        int userCount = 6;
        List<PossibleTimeCaseVo> response = List.of(
                new PossibleTimeCaseVo(TWO_HOUR, 6),
                new PossibleTimeCaseVo(HOUR_HALF, 6),
                new PossibleTimeCaseVo(TWO_HOUR, 5),
                new PossibleTimeCaseVo(HOUR_HALF, 5),
                new PossibleTimeCaseVo(TWO_HOUR, 4),
                new PossibleTimeCaseVo(HOUR_HALF, 4),
                new PossibleTimeCaseVo(HOUR, 6),
                new PossibleTimeCaseVo(HOUR, 5),
                new PossibleTimeCaseVo(HOUR, 4),
                new PossibleTimeCaseVo(HALF, 6),
                new PossibleTimeCaseVo(HALF, 5),
                new PossibleTimeCaseVo(HALF, 4),
                new PossibleTimeCaseVo(TWO_HOUR, 3),
                new PossibleTimeCaseVo(HOUR_HALF, 3),
                new PossibleTimeCaseVo(TWO_HOUR, 2),
                new PossibleTimeCaseVo(HOUR_HALF, 2),
                new PossibleTimeCaseVo(HOUR, 3),
                new PossibleTimeCaseVo(HOUR, 2),
                new PossibleTimeCaseVo(HALF, 3),
                new PossibleTimeCaseVo(HALF, 2),
                new PossibleTimeCaseVo(TWO_HOUR, 1),
                new PossibleTimeCaseVo(HOUR_HALF, 1),
                new PossibleTimeCaseVo(HOUR, 1),
                new PossibleTimeCaseVo(HALF, 1)
        );

        // when
        List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

        // then
        assertThat(result.size()).isEqualTo(response.size());
        assertThat(result).isEqualTo(response);
    }

    @DisplayName("회의 진행 시간이 30분이고, 회의 참여 인원이 1명일 때 1개의 경우의 수가 나온다.")
    @Test
    void findOptimalMeetingTimeCasesTest2() {
        // given
        Duration duration = HALF;
        int userCount = 1;
        List<PossibleTimeCaseVo> response = List.of(
                new PossibleTimeCaseVo(HALF, 1)
        );

        // when
        List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

        // then
        assertThat(result.size()).isEqualTo(response.size());
        assertThat(result).isEqualTo(response);
    }

    @DisplayName("회의 진행 시간이 1시간이고, 회의 참여 인원이 1명일 때 2개의 경우의 수가 나온다.")
    @Test
    void findOptimalMeetingTimeCasesTest3() {
        // given
        Duration duration = HOUR;
        int userCount = 1;
        List<PossibleTimeCaseVo> response = List.of(
                new PossibleTimeCaseVo(HOUR, 1),
                new PossibleTimeCaseVo(HALF, 1)
        );

        // when
        List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

        // then
        assertThat(result.size()).isEqualTo(response.size());
        assertThat(result).isEqualTo(response);
    }

    @DisplayName("회의 진행 시간이 1시간 30분이고, 회의 참여 인원이 2명일 때 6개의 경우의 수가 나온다.")
    @Test
    void findOptimalMeetingTimeCasesTest4() {
        // given
        Duration duration = HOUR_HALF;
        int userCount = 2;
        List<PossibleTimeCaseVo> response = List.of(
                new PossibleTimeCaseVo(HOUR_HALF, 2),
                new PossibleTimeCaseVo(HOUR, 2),
                new PossibleTimeCaseVo(HALF, 2),
                new PossibleTimeCaseVo(HOUR_HALF, 1),
                new PossibleTimeCaseVo(HOUR, 1),
                new PossibleTimeCaseVo(HALF, 1)
        );

        // when
        List<PossibleTimeCaseVo> result = strategy.find(duration, userCount);

        // then
        assertThat(result.size()).isEqualTo(response.size());
        assertThat(result).isEqualTo(response);
    }
}