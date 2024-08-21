package com.asap.server.service.meeting.recommend;

import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_13_30;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_15_00;
import static com.asap.server.persistence.domain.enums.TimeSlot.SLOT_16_00;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.meeting.recommend.strategy.impl.BestMeetingTimeStrategyImpl;
import com.asap.server.service.meeting.recommend.strategy.impl.ContinuousMeetingTimeStrategyImpl;
import com.asap.server.service.meeting.recommend.strategy.impl.MeetingTimeCasesStrategyImpl;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.vo.BestMeetingTimeVo;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingTimeRecommendServiceTest {
    private MeetingTimeRecommendService meetingTimeRecommendService;

    @BeforeEach
    public void setUp() {
        meetingTimeRecommendService = new MeetingTimeRecommendService(
                new MeetingTimeCasesStrategyImpl(),
                new ContinuousMeetingTimeStrategyImpl(),
                new BestMeetingTimeStrategyImpl()
        );
    }

    @Test
    @DisplayName("최적의 회의시간이 하나만 있는 경우")
    public void getBestMeetingTime() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        TimeBlockVo timeBlock = new TimeBlockVo(availableDate, SLOT_12_00, 0, List.of(1L));
        List<TimeBlockVo> timeBlocks = List.of(timeBlock);

        BestMeetingTimeVo expected = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);

        // when
        List<BestMeetingTimeVo> result = meetingTimeRecommendService.getBestMeetingTime(timeBlocks, Duration.HALF, 1);

        // then
        assertThat(result).isEqualTo(Arrays.asList(expected, null, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 2개만 있는 경우")
    public void getBestMeetingTime2() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlock = new TimeBlockVo(availableDate, SLOT_12_00, 0, List.of(1L));
        TimeBlockVo timeBlock2 = new TimeBlockVo(availableDate2, SLOT_12_30, 0, List.of(1L));
        List<TimeBlockVo> timeBlocks = List.of(timeBlock, timeBlock2);

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_00, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, null);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService
                .getBestMeetingTime(timeBlocks, Duration.HALF, 1);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의시간이 3개만 있는 경우")
    public void getBestMeetingTime3() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlock = new TimeBlockVo(availableDate, SLOT_12_00, 0, List.of(1L, 2L));
        TimeBlockVo timeBlock2 = new TimeBlockVo(availableDate, SLOT_13_00, 0, List.of(1L, 2L));
        TimeBlockVo timeBlock3 = new TimeBlockVo(availableDate2, SLOT_12_30, 0, List.of(1L, 2L));
        TimeBlockVo timeBlock4 = new TimeBlockVo(availableDate2, SLOT_13_00, 0, List.of(1L, 2L));
        List<TimeBlockVo> timeBlocks = List.of(timeBlock, timeBlock2, timeBlock3, timeBlock4);

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, SLOT_13_00, SLOT_13_30, 0);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_00, 0);
        List<BestMeetingTimeVo> expected = List.of(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService
                .getBestMeetingTime(timeBlocks, Duration.HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의시간이 2개이고 차선의 경우가 1개 있는 경우")
    public void getBestMeetingTime4() {
        // given
        LocalDate availableDate = LocalDate.of(2024, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2024, 7, 11);
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L))
        );

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_00, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, SLOT_15_00, SLOT_16_00, 0);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_30, 0);
        List<BestMeetingTimeVo> expected = List.of(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService
                .getBestMeetingTime(timeBlocks, Duration.HOUR, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의 시간이 3개일 때 우선순위가 가장 높은 회의 시간이 첫번째에 위치한다")
    public void getBestMeetingTime5() {
        // given
        LocalDate availableDate = LocalDate.of(2024, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2024, 7, 11);

        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_12_30, 6, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 11), TimeSlot.SLOT_13_00, 6, List.of(1L, 2L))
        );

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_30, 6);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_00, 0);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate, SLOT_15_00, SLOT_16_00, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService
                .getBestMeetingTime(timeBlocks, Duration.HOUR, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("회의 시간이 30분이고, time block 이 1개 일 때, (해당 시간, null, null) 을 반환한다.")
    public void getBestMeetingTime6() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);

        TimeBlockVo timeBlock = new TimeBlockVo(availableDate, SLOT_12_00, 0, List.of(1L, 2L));
        List<TimeBlockVo> timeBlocks = List.of(timeBlock);

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, null, null);

        // when
        List<BestMeetingTimeVo> result = meetingTimeRecommendService.getBestMeetingTime(timeBlocks, Duration.HALF, 2);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("가중치가 3인 최적의 회의 시간이 1개이고, 가중치가 4인 차선의 회의 시간이 4개일 때 (최적의 회의 시간 , 차선의 회의 시간 2개) 순으로 반환한다.")
    public void getBestMeetingTime7() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);
        LocalDate availableDate3 = LocalDate.of(2023, 7, 12);

        TimeBlockVo timeBlock = new TimeBlockVo(availableDate, SLOT_12_00, 3, List.of(1L, 2L, 3L));
        TimeBlockVo timeBlock2 = new TimeBlockVo(availableDate, SLOT_12_30, 3, List.of(1L, 2L, 3L));
        TimeBlockVo timeBlock3 = new TimeBlockVo(availableDate2, SLOT_12_00, 4, List.of(1L, 2L, 3L));
        TimeBlockVo timeBlock4 = new TimeBlockVo(availableDate3, SLOT_12_00, 4, List.of(1L, 2L, 3L));

        List<TimeBlockVo> timeBlocks = List.of(timeBlock, timeBlock2, timeBlock3, timeBlock4);

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_00, 3);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate2, SLOT_12_00, SLOT_12_30, 4);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate3, SLOT_12_00, SLOT_12_30, 4);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> result = meetingTimeRecommendService.getBestMeetingTime(timeBlocks, Duration.HOUR, 3);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 추천한 시간대는 이후 조합에서 추천하지 않는다.")
    public void getBestMeetingTime8() {
        // given
        LocalDate availableDate = LocalDate.of(2024, 7, 10);
        List<TimeBlockVo> timeBlocks = List.of(
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_30, 0, List.of(1L, 2L)),
                new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_15_00, 0, List.of(1L, 2L))
        );

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_30, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, null, null);

        // when
        List<BestMeetingTimeVo> result = meetingTimeRecommendService.getBestMeetingTime(timeBlocks, Duration.HOUR_HALF, 2);

        // then
        assertThat(result).isEqualTo(expected);
    }
}