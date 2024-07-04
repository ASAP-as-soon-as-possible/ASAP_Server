package com.asap.server.service.meeting;

import com.asap.server.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.meeting.FindBestMeetingTimeStrategy;
import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.asap.server.domain.enums.TimeSlot.SLOT_11_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_11_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_13_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_20_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_20_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_21_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_21_30;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FindBestMeetingTimeStrategyTest {
    private FindBestMeetingTimeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FindBestMeetingTimeStrategy();
    }

    @Test
    @DisplayName("모든 유저들이 특정 날짜에 회의 시간이 가능할 경우")
    public void getBestMeetingTime() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 9, 8);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_11_00, 0, 2L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_11_30, 0, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate, SLOT_12_00, 0, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate, SLOT_12_30, 0, 2L);
        TimeBlockDto timeBlock5 = new TimeBlockDto(availableDate, SLOT_13_00, 0, 2L);
        TimeBlockDto timeBlock6 = new TimeBlockDto(availableDate, SLOT_13_30, 0, 2L);
        TimeBlockDto timeBlock7 = new TimeBlockDto(availableDate, SLOT_14_00, 0, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4, timeBlock5, timeBlock6, timeBlock7));

        BestMeetingTimeVo bestMeetingTime = new BestMeetingTimeVo(availableDate, SLOT_11_00, SLOT_13_00, 0);
        BestMeetingTimeVo bestMeetingTime2 = new BestMeetingTimeVo(availableDate, SLOT_11_30, SLOT_13_30, 0);
        BestMeetingTimeVo bestMeetingTime3 = new BestMeetingTimeVo(availableDate, SLOT_12_00, SLOT_14_00, 0);
        BestMeetingTimeVo bestMeetingTime4 = new BestMeetingTimeVo(availableDate, SLOT_12_30, SLOT_14_30, 0);
        List<BestMeetingTimeVo> expected = new ArrayList<>(List.of(bestMeetingTime, bestMeetingTime2, bestMeetingTime3, bestMeetingTime4));

        PossibleTimeCaseVo possibleTimeCaseVo = new PossibleTimeCaseVo(Duration.TWO_HOUR, 2);

        // when
        List<BestMeetingTimeVo> result = strategy.find(timeBlocks, possibleTimeCaseVo);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 날짜에 최적의 회의 시간대가 아침에 하나 저녁에 하나 있을 경우")
    public void getBestMeetingTime2() {
        // given
        LocalDate availableDate = LocalDate.of(2023, 9, 8);

        TimeBlockDto timeBlock = new TimeBlockDto(availableDate, SLOT_11_00, 0, 2L);
        TimeBlockDto timeBlock2 = new TimeBlockDto(availableDate, SLOT_11_30, 0, 2L);
        TimeBlockDto timeBlock3 = new TimeBlockDto(availableDate, SLOT_12_00, 0, 2L);
        TimeBlockDto timeBlock4 = new TimeBlockDto(availableDate, SLOT_12_30, 0, 1L);
        TimeBlockDto timeBlock5 = new TimeBlockDto(availableDate, SLOT_13_00, 0, 1L);
        TimeBlockDto timeBlock6 = new TimeBlockDto(availableDate, SLOT_13_30, 0, 1L);
        TimeBlockDto timeBlock7 = new TimeBlockDto(availableDate, SLOT_20_00, 0, 2L);
        TimeBlockDto timeBlock8 = new TimeBlockDto(availableDate, SLOT_20_30, 0, 2L);
        TimeBlockDto timeBlock9 = new TimeBlockDto(availableDate, SLOT_21_00, 0, 2L);
        List<TimeBlockDto> timeBlocks = new ArrayList<>(Arrays.asList(timeBlock, timeBlock2, timeBlock3, timeBlock4, timeBlock5, timeBlock6, timeBlock7, timeBlock8, timeBlock9));

        BestMeetingTimeVo bestMeetingTime = new BestMeetingTimeVo(availableDate, SLOT_11_00, SLOT_12_00, 0);
        BestMeetingTimeVo bestMeetingTime2 = new BestMeetingTimeVo(availableDate, SLOT_11_30, SLOT_12_30, 0);
        BestMeetingTimeVo bestMeetingTime3 = new BestMeetingTimeVo(availableDate, SLOT_20_00, SLOT_21_00, 0);
        BestMeetingTimeVo bestMeetingTime4 = new BestMeetingTimeVo(availableDate, SLOT_20_30, SLOT_21_30, 0);
        List<BestMeetingTimeVo> expected = new ArrayList<>(List.of(bestMeetingTime, bestMeetingTime2, bestMeetingTime3, bestMeetingTime4));

        PossibleTimeCaseVo possibleTimeCaseVo = new PossibleTimeCaseVo(Duration.HOUR, 2);

        // when
        List<BestMeetingTimeVo> result = strategy.find(timeBlocks, possibleTimeCaseVo);

        // then
        assertThat(result).isEqualTo(expected);
    }
}