package com.asap.server.service.meeting;

import com.asap.server.domain.enums.Duration;
import com.asap.server.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.vo.BestMeetingTimeVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.asap.server.domain.enums.TimeSlot.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MeetingTimeRecommendServiceTest {
    private MeetingTimeRecommendService meetingTimeRecommendService;

    @BeforeEach
    public void setUp() {
        meetingTimeRecommendService = new MeetingTimeRecommendService(
                new FindBestMeetingTimeCasesStrategy(),
                new FindBestMeetingTimeStrategy()
        );
    }

    @Test
    @DisplayName("최적의 회의시간이 하나만 있는 경우")
    public void getBestMeetingTime() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 1L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock));

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

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 1L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate2, SLOT_12_30, 0, 1L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock, timeBlock2));

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_00, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, null);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(timeBlocks,
                Duration.HALF, 1);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의시간이 3개만 있는 경우")
    public void getBestMeetingTime3() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 2L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate2, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate2, SLOT_13_00, 0, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4));

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, SLOT_12_30, SLOT_13_00, 0);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_00, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(timeBlocks,
                Duration.HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의시간이 2개이고 차선의 경우가 1개 있는 경우")
    public void getBestMeetingTime4() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 2L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate2, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate2, SLOT_13_00, 0, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4));

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_00, 0);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_13_30, 0);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(timeBlocks,
                Duration.HOUR, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("최적의 회의 시간이 3개일 때 우선순위가 가장 높은 회의 시간이 첫번째에 위치한다")
    public void getBestMeetingTime5() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);
        LocalDate availableDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 2L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate, SLOT_13_00, 0, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate, SLOT_13_30, 0, 2L);
        TimeBlockDto timeBlock5 = new TimeBlockDto(availableDate2, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock6 = new TimeBlockDto(availableDate2, SLOT_13_00, 6, 2L);
        TimeBlockDto timeBlock7 = new TimeBlockDto(availableDate2, SLOT_13_30, 6, 2L);
        TimeBlockDto timeBlock8 = new TimeBlockDto(availableDate2, SLOT_14_00, 6, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(
                Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4, timeBlock5, timeBlock6, timeBlock7,
                        timeBlock8));

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate2, SLOT_13_00, SLOT_14_30, 18);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate2, SLOT_12_30, SLOT_14_00, 12);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_13_30, 0);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(timeBlocks,
                Duration.HOUR_HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(expected);
    }

    @Test
    @DisplayName("회의 시간이 30분이고, time block 이 1개 일 때, (해당 시간, null, null) 을 반환한다.")
    public void getBestMeetingTime6() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 7, 10);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 0, 1L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock));

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

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_12_00, 3, 3L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_12_30, 4, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate, SLOT_13_00, 4, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate, SLOT_13_30, 4, 2L);
        TimeBlockDto timeBlock5 = new TimeBlockDto(availableDate, SLOT_14_00, 4, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(
                Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4, timeBlock5));

        BestMeetingTimeVo e1 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_12_30, 3);
        BestMeetingTimeVo e2 = new BestMeetingTimeVo(availableDate, SLOT_12_30, SLOT_13_00, 4);
        BestMeetingTimeVo e3 = new BestMeetingTimeVo(availableDate, SLOT_13_00, SLOT_13_30, 4);
        List<BestMeetingTimeVo> expected = Arrays.asList(e1, e2, e3);

        // when
        List<BestMeetingTimeVo> result = meetingTimeRecommendService.getBestMeetingTime(timeBlocks, Duration.HALF, 3);

        // then
        assertThat(result).isEqualTo(expected);
    }
}