package com.asap.server.common.utils;

import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import com.asap.server.service.vo.UserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.asap.server.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_13_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_30;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetBestMeetingTimeTest {
    private BestMeetingUtil bestMeetingUtil;

    @BeforeEach
    public void setUp() {
        bestMeetingUtil = new BestMeetingUtil();
    }

    @Test
    @DisplayName("최적의 회의시간이 하나만 있는 경우")
    public void getBestMeetingTime() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        List<UserVo> users = List.of(userVo);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate));

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_12_30, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HALF, 1);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, null, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 2개만 있는 경우")
    public void getBestMeetingTime2() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        List<UserVo> users = List.of(userVo);

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);
        LocalDate meetingDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate));

        TimeBlockVo timeBlockByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        List<TimeBlockVo> timeBlocks2 = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate2));

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        TimeBlocksByDateVo timeBlocksByDate2 = new TimeBlocksByDateVo(2L, meetingDate2, timeBlocks2);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate, timeBlocksByDate2);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_12_30, users, 0);
        BestMeetingTimeVo result2 = new BestMeetingTimeVo(meetingDate2, SLOT_12_30, SLOT_13_00, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HALF, 1);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, result2, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 3개만 있는 경우")
    public void getBestMeetingTime3() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        UserVo userVo2 = new UserVo(2L, "도소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);
        LocalDate meetingDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        TimeBlockVo timeBlock2ByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate, timeBlock2ByMeetingDate));

        TimeBlockVo timeBlockByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        TimeBlockVo timeBlock2ByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_13_00, users);
        List<TimeBlockVo> timeBlocks2 = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate2, timeBlock2ByMeetingDate2));

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        TimeBlocksByDateVo timeBlocksByDate2 = new TimeBlocksByDateVo(2L, meetingDate2, timeBlocks2);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate, timeBlocksByDate2);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_12_30, users, 0);
        BestMeetingTimeVo result2 = new BestMeetingTimeVo(meetingDate, SLOT_12_30, SLOT_13_00, users, 0);
        BestMeetingTimeVo result3 = new BestMeetingTimeVo(meetingDate2, SLOT_12_30, SLOT_13_00, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, result2, result3));
    }

    @Test
    @DisplayName("최적의 회의시간이 2개이고 차선의 경우가 1개 있는 경우")
    public void getBestMeetingTime4() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        UserVo userVo2 = new UserVo(2L, "도소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);
        LocalDate meetingDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        TimeBlockVo timeBlock2ByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate, timeBlock2ByMeetingDate));

        TimeBlockVo timeBlockByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        TimeBlockVo timeBlock2ByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_13_00, users);
        List<TimeBlockVo> timeBlocks2 = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate2, timeBlock2ByMeetingDate2));

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        TimeBlocksByDateVo timeBlocksByDate2 = new TimeBlocksByDateVo(2L, meetingDate2, timeBlocks2);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate, timeBlocksByDate2);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_13_00, users, 0);
        BestMeetingTimeVo result2 = new BestMeetingTimeVo(meetingDate2, SLOT_12_30, SLOT_13_30, users, 0);
        BestMeetingTimeVo result3 = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_12_30, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HOUR, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, result2, result3));
    }

    @Test
    @DisplayName("최적의 회의 시간이 3개일 때 우선순위가 가장 높은 회의 시간이 첫번째에 위치한다")
    public void getBestMeetingTime5() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        UserVo userVo2 = new UserVo(2L, "도소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);
        LocalDate meetingDate2 = LocalDate.of(2023, 7, 11);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        TimeBlockVo timeBlock2ByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        TimeBlockVo timeBlock3ByMeetingDate = new TimeBlockVo(1L, 0, SLOT_13_00, users);
        TimeBlockVo timeBlock4ByMeetingDate = new TimeBlockVo(1L, 0, SLOT_13_30, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate, timeBlock2ByMeetingDate, timeBlock3ByMeetingDate, timeBlock4ByMeetingDate));

        TimeBlockVo timeBlockByMeetingDate2 = new TimeBlockVo(1L, 0, SLOT_12_30, users);
        TimeBlockVo timeBlock2ByMeetingDate2 = new TimeBlockVo(1L, 6, SLOT_13_00, users);
        TimeBlockVo timeBlock3ByMeetingDate2 = new TimeBlockVo(1L, 6, SLOT_13_30, users);
        TimeBlockVo timeBlock4ByMeetingDate2 = new TimeBlockVo(1L, 6, SLOT_14_00, users);
        List<TimeBlockVo> timeBlocks2 = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate2, timeBlock2ByMeetingDate2, timeBlock3ByMeetingDate2, timeBlock4ByMeetingDate2));

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        TimeBlocksByDateVo timeBlocksByDate2 = new TimeBlocksByDateVo(2L, meetingDate2, timeBlocks2);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate, timeBlocksByDate2);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate2, SLOT_13_00, SLOT_14_30, users, 18);
        BestMeetingTimeVo result2 = new BestMeetingTimeVo(meetingDate2, SLOT_12_30, SLOT_14_00, users, 12);
        BestMeetingTimeVo result3 = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_13_30, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HOUR_HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, result2, result3));
    }

    @Test
    @DisplayName("회의 시간이 30분이고, time block 이 1개 일 때, (해당 시간, null, null) 을 반환한다.")
    public void getBestMeetingTime6() {
        // given
        UserVo userVo = new UserVo(1L, "강원용");
        List<UserVo> users = Arrays.asList(userVo);

        LocalDate meetingDate = LocalDate.of(2023, 7, 10);

        TimeBlockVo timeBlockByMeetingDate = new TimeBlockVo(1L, 0, SLOT_12_00, users);
        List<TimeBlockVo> timeBlocks = new ArrayList<>(Arrays.asList(timeBlockByMeetingDate));

        TimeBlocksByDateVo timeBlocksByDate = new TimeBlocksByDateVo(1L, meetingDate, timeBlocks);
        List<TimeBlocksByDateVo> timeBlocksByDates = Arrays.asList(timeBlocksByDate);

        BestMeetingTimeVo result = new BestMeetingTimeVo(meetingDate, SLOT_12_00, SLOT_12_30, users, 0);

        // when
        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(timeBlocksByDates, Duration.HALF, 2);

        // then
        assertThat(bestMeetingTimes).isEqualTo(Arrays.asList(result, null, null));
    }
}
