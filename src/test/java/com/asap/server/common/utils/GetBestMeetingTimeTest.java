package com.asap.server.common.utils;

import com.asap.server.service.vo.AvailableMeetingTimeVo;
import com.asap.server.service.vo.DateAvailabilityVo;
import com.asap.server.service.vo.MeetingVo;
import com.asap.server.domain.enums.Duration;
import com.asap.server.service.vo.UserVo;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.asap.server.domain.enums.TimeSlot.SLOT_12_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_12_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_13_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_14_30;
import static com.asap.server.domain.enums.TimeSlot.SLOT_15_00;
import static com.asap.server.domain.enums.TimeSlot.SLOT_18_00;
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
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);

        UserVo userVo = new UserVo(1L, "강원용");
        List<UserVo> users = List.of(userVo);

        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", SLOT_12_00, SLOT_12_30, userVo, 0);
        List<MeetingTimeVo> meetingTimes = List.of(meetingTimeVo);

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo("7.10.월", SLOT_12_00, SLOT_12_30, 0, List.of(userVo), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingVo, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, null, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 2개만 있는 경우")
    public void getBestMeetingTime2() {
        // given
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);

        UserVo userVo = new UserVo(1L, "강원용");
        List<UserVo> users = List.of(userVo);

        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.HALF, users);

        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", SLOT_12_00, SLOT_13_00, userVo, 0);
        List<MeetingTimeVo> meetingTimes = List.of(meetingTimeVo);

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo("7.10.월", SLOT_12_00, SLOT_12_30, 0, List.of(userVo), true);
        AvailableMeetingTimeVo result2 = new AvailableMeetingTimeVo("7.10.월", SLOT_12_30, SLOT_13_00, 0, List.of(userVo), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingVo, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 3개만 있는 경우")
    public void getBestMeetingTime3() {
        // given
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);

        UserVo userVo = new UserVo(1L, "강원용");
        UserVo userVo2 = new UserVo(2L, "도소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);

        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", SLOT_12_00, SLOT_18_00, userVo, 0);
        MeetingTimeVo meetingTimeVo2 = new MeetingTimeVo("7", "11", "화", SLOT_12_00, SLOT_18_00, userVo2, 0);
        List<MeetingTimeVo> meetingTimes = Arrays.asList(meetingTimeVo, meetingTimeVo2);

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo("7.10.월", SLOT_12_00, SLOT_14_00, 0, List.of(userVo), true);
        AvailableMeetingTimeVo result2 = new AvailableMeetingTimeVo("7.10.월", SLOT_12_30, SLOT_14_30, 0, List.of(userVo), true);
        AvailableMeetingTimeVo result3 = new AvailableMeetingTimeVo("7.10.월", SLOT_13_00, SLOT_15_00, 0, List.of(userVo), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingVo, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, result3));
    }
}
