package com.asap.server.common.utils;

import com.asap.server.service.vo.AvailableMeetingTimeVo;
import com.asap.server.service.vo.DateAvailabilityVo;
import com.asap.server.service.vo.MeetingVo;
import com.asap.server.service.vo.PossibleTimeCaseVo;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.UserVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.asap.server.domain.enums.Duration.HALF;
import static com.asap.server.domain.enums.Duration.HOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BestMeetingUtilTest {
    private BestMeetingUtil bestMeetingUtil;

    @BeforeEach
    public void setUp() {
        bestMeetingUtil = new BestMeetingUtil();
    }

    @Test
    @DisplayName("initTimeTable 테스트")
    public void initTimeTableTest() {
        // given
        ReflectionTestUtils.setField(bestMeetingUtil, "timeTable", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "availableMeetingTimesByDuration", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "timeCases", new ArrayList<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "fixedMeetingTime", new ArrayList<>());
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        DateAvailabilityVo dateAvailability3 = new DateAvailabilityVo("7", "12", "수");
        UserVo userVo = new UserVo(1L, "심은서");
        UserVo userVo2 = new UserVo(2L, "이동헌");
        UserVo userVo3 = new UserVo(3L, "이재훈");
        List<UserVo> users = Arrays.asList(userVo, userVo2, userVo3);
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingVo);

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "initTimeTable");

        // then
        assertThat(bestMeetingUtil.getMeeting().getAvailabilities().size()).isEqualTo(3);
        assertThat(bestMeetingUtil.getTimeTable().get("7.10.월")).isNotNull();
        assertThat(bestMeetingUtil.getTimeTable().get("7.11.화")).isNotNull();
        assertThat(bestMeetingUtil.getTimeTable().get("7.12.수")).isNotNull();
        assertThat(bestMeetingUtil.getTimeTable().get("7.13.목")).isNull();
    }

    @Test
    @DisplayName("해당 회의의 meeting time 들을 time table 안에 넣는다.")
    public void setUserMeetingTimeTest() {
        // given
        ReflectionTestUtils.setField(bestMeetingUtil, "timeTable", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "availableMeetingTimesByDuration", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "timeCases", new ArrayList<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "fixedMeetingTime", new ArrayList<>());
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        DateAvailabilityVo dateAvailability3 = new DateAvailabilityVo("7", "12", "수");
        UserVo userVo = new UserVo(1L, "원용");
        UserVo userVo2 = new UserVo(2L, "소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, userVo, 0);
        MeetingTimeVo meetingTimeVo2 = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_16_00, TimeSlot.SLOT_18_00, userVo2, 0);
        MeetingTimeVo meetingTimeVo3 = new MeetingTimeVo("7", "11", "화", TimeSlot.SLOT_11_00, TimeSlot.SLOT_12_00, userVo, 0);
        MeetingTimeVo meetingTimeVo4 = new MeetingTimeVo("7", "11", "화", TimeSlot.SLOT_11_00, TimeSlot.SLOT_12_00, userVo2, 0);
        MeetingTimeVo meetingTimeVo5 = new MeetingTimeVo("7", "12", "수", TimeSlot.SLOT_7_00, TimeSlot.SLOT_9_00, userVo, 2);
        MeetingTimeVo meetingTimeVo6 = new MeetingTimeVo("7", "12", "수", TimeSlot.SLOT_7_00, TimeSlot.SLOT_9_00, userVo2, 1);

        List<MeetingTimeVo> meetingTimes = Arrays.asList(meetingTimeVo, meetingTimeVo2, meetingTimeVo3, meetingTimeVo4, meetingTimeVo5, meetingTimeVo6);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingVo);
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "initTimeTable");

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "setUserMeetingTime", meetingTimes);

        // then
        assertThat(bestMeetingUtil.getTimeTable().get("7.11.화").get(TimeSlot.SLOT_11_00).getUsers()).isEqualTo(Arrays.asList(userVo, userVo2));
        assertThat(bestMeetingUtil.getTimeTable().get("7.12.수").get(TimeSlot.SLOT_7_00).getUsers()).isEqualTo(Arrays.asList(userVo, userVo2));
        assertThat(bestMeetingUtil.getTimeTable().get("7.12.수").get(TimeSlot.SLOT_7_00).getWeight()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 회의 시간 동안 가능한 회의 시간들을 모두 구한다.")
    public void collectAvailableMeetingTimeByDurationTest() {
        // given
        ReflectionTestUtils.setField(bestMeetingUtil, "timeTable", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "availableMeetingTimesByDuration", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "timeCases", new ArrayList<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "fixedMeetingTime", new ArrayList<>());
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);
        UserVo userVo = new UserVo(1L, "원용");
        UserVo userVo2 = new UserVo(2L, "소현");
        List<UserVo> users = Arrays.asList(userVo, userVo2);
        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, userVo, 0);
        MeetingTimeVo meetingTimeVo2 = new MeetingTimeVo("7", "11", "화", TimeSlot.SLOT_12_00, TimeSlot.SLOT_14_00, userVo, 0);
        MeetingTimeVo meetingTimeVo3 = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, userVo2, 0);
        MeetingTimeVo meetingTimeVo4 = new MeetingTimeVo("7", "11", "화", TimeSlot.SLOT_12_00, TimeSlot.SLOT_14_00, userVo2, 0);
        List<MeetingTimeVo> meetingTimes = Arrays.asList(meetingTimeVo, meetingTimeVo2, meetingTimeVo3, meetingTimeVo4);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingVo);
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "initTimeTable");
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "setUserMeetingTime", meetingTimes);

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo(
                "7.10.월",
                TimeSlot.SLOT_18_00,
                TimeSlot.SLOT_20_00,
                0,
                Arrays.asList(userVo, userVo2),
                false
        );

        AvailableMeetingTimeVo result2 = new AvailableMeetingTimeVo(
                "7.11.화",
                TimeSlot.SLOT_12_00,
                TimeSlot.SLOT_14_00,
                0,
                Arrays.asList(userVo, userVo2),
                false
        );

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "collectAvailableMeetingTimeByDuration", Duration.TWO_HOUR);

        // then
        assertThat(bestMeetingUtil.getAvailableMeetingTimesByDuration().get(Duration.TWO_HOUR)).isEqualTo(Arrays.asList(result, result2));
    }

    @Test
    @DisplayName("회의시간과 참여인원 수가 있을 때 가능한 시간대와 인원수를 모두 담는다.")
    public void timeCasesTest2() {
        // given
        ReflectionTestUtils.setField(bestMeetingUtil, "timeTable", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "availableMeetingTimesByDuration", new HashMap<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "timeCases", new ArrayList<>());
        ReflectionTestUtils.setField(bestMeetingUtil, "fixedMeetingTime", new ArrayList<>());
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);
        UserVo userVo = new UserVo(1L, "심은서");
        UserVo userVo2 = new UserVo(2L, "이동헌");
        UserVo userVo3 = new UserVo(3L, "이재훈");
        UserVo userVo4 = new UserVo(3L, "정찬우");
        List<UserVo> users = Arrays.asList(userVo, userVo2, userVo3, userVo4);
        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, HOUR, users);

        PossibleTimeCaseVo tc = new PossibleTimeCaseVo(HOUR, 4);
        PossibleTimeCaseVo tc2 = new PossibleTimeCaseVo(HALF, 4);
        PossibleTimeCaseVo tc3 = new PossibleTimeCaseVo(HOUR, 3);
        PossibleTimeCaseVo tc4 = new PossibleTimeCaseVo(HALF, 3);
        PossibleTimeCaseVo tc5 = new PossibleTimeCaseVo(HOUR, 2);
        PossibleTimeCaseVo tc6 = new PossibleTimeCaseVo(HALF, 2);
        PossibleTimeCaseVo tc7 = new PossibleTimeCaseVo(HOUR, 1);
        PossibleTimeCaseVo tc8 = new PossibleTimeCaseVo(HALF, 1);
        List<PossibleTimeCaseVo> timeCases = Arrays.asList(tc, tc2, tc3, tc4, tc5, tc6, tc7, tc8);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingVo);

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "getAllPossibleMeetingTimeCases", meetingVo.getDuration());

        // then
        // assertThat(timeCases).isEqualTo(bestMeetingUtil.getTimeCases());
    }

    @Test
    @DisplayName("최종 회의시간 도출하기")
    public void getBestMeetingTime() {
        // given
        DateAvailabilityVo dateAvailability1 = new DateAvailabilityVo("7", "10", "월");
        DateAvailabilityVo dateAvailability2 = new DateAvailabilityVo("7", "11", "화");
        List<DateAvailabilityVo> dateAvailabilityVo = Arrays.asList(dateAvailability1, dateAvailability2);
        UserVo userVo = new UserVo(1L, "심은서");
        UserVo userVo2 = new UserVo(2L, "이동헌");
        UserVo userVo3 = new UserVo(3L, "이재훈");
        List<UserVo> users = Arrays.asList(userVo, userVo2, userVo3);
        MeetingVo meetingVo = new MeetingVo(dateAvailabilityVo, Duration.TWO_HOUR, users);

        // 14~16 , 14:00 15:30 , 14:30 16:00
        MeetingTimeVo meetingTimeVo = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_12_00, TimeSlot.SLOT_20_00, userVo, 0);
        MeetingTimeVo meetingTimeVo2 = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_12_00, TimeSlot.SLOT_16_00, userVo2, 0);
        MeetingTimeVo meetingTimeVo3 = new MeetingTimeVo("7", "10", "월", TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, userVo3, 0);
        List<MeetingTimeVo> meetingTimes = Arrays.asList(meetingTimeVo, meetingTimeVo2, meetingTimeVo3);

        AvailableMeetingTimeVo result = new AvailableMeetingTimeVo(
                "7.10.월",
                TimeSlot.SLOT_14_00,
                TimeSlot.SLOT_16_00,
                0,
                Arrays.asList(userVo, userVo2, userVo3),
                true
        );

        AvailableMeetingTimeVo result2 = new AvailableMeetingTimeVo(
                "7.10.월",
                TimeSlot.SLOT_14_00,
                TimeSlot.SLOT_15_30,
                0,
                Arrays.asList(userVo, userVo2, userVo3),
                true
        );
        AvailableMeetingTimeVo result3 = new AvailableMeetingTimeVo(
                "7.10.월",
                TimeSlot.SLOT_14_30,
                TimeSlot.SLOT_16_00,
                0,
                Arrays.asList(userVo, userVo2, userVo3),
                true
        );

        // when
        bestMeetingUtil.getBestMeetingTime(meetingVo, meetingTimes);

        // then
        assertAll(
                () -> assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, result3))
        );
    }
}
