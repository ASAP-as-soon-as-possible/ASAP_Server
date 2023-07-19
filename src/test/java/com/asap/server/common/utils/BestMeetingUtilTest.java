package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableMeetingTimeDto;
import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.PossibleTimeCaseDto;
import com.asap.server.controller.dto.response.UserDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import java.util.Arrays;
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
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        DateAvailabilityDto dateAvailability3 = new DateAvailabilityDto("7", "12", "수");
        UserDto userDto = new UserDto(1L, "심은서");
        UserDto userDto2 = new UserDto(2L, "이동헌");
        UserDto userDto3 = new UserDto(3L, "이재훈");
        List<UserDto> users = Arrays.asList(userDto, userDto2, userDto3);
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingDto);

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
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        DateAvailabilityDto dateAvailability3 = new DateAvailabilityDto("7", "12", "수");
        UserDto userDto = new UserDto(1L, "심은서");
        UserDto userDto2 = new UserDto(2L, "이동헌");
        UserDto userDto3 = new UserDto(3L, "이재훈");
        List<UserDto> users = Arrays.asList(userDto, userDto2, userDto3);
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, "원용", 0);
        MeetingTimeDto meetingTimeDto2 = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_16_00, TimeSlot.SLOT_18_00, "소현", 0);
        MeetingTimeDto meetingTimeDto3 = new MeetingTimeDto("7", "11", "화", TimeSlot.SLOT_11_00, TimeSlot.SLOT_12_00, "원용", 0);
        MeetingTimeDto meetingTimeDto4 = new MeetingTimeDto("7", "11", "화", TimeSlot.SLOT_11_00, TimeSlot.SLOT_12_00, "소현", 0);
        MeetingTimeDto meetingTimeDto5 = new MeetingTimeDto("7", "12", "수", TimeSlot.SLOT_7_00, TimeSlot.SLOT_9_00, "원용", 2);
        MeetingTimeDto meetingTimeDto6 = new MeetingTimeDto("7", "12", "수", TimeSlot.SLOT_7_00, TimeSlot.SLOT_9_00, "소현", 1);

        List<MeetingTimeDto> meetingTimes = Arrays.asList(meetingTimeDto, meetingTimeDto2, meetingTimeDto3, meetingTimeDto4, meetingTimeDto5, meetingTimeDto6);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingDto);
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "initTimeTable");

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "setUserMeetingTime", meetingTimes);

        // then
        assertThat(bestMeetingUtil.getTimeTable().get("7.11.화").get(TimeSlot.SLOT_11_00).getUserNames()).isEqualTo(Arrays.asList("원용", "소현"));
        assertThat(bestMeetingUtil.getTimeTable().get("7.12.수").get(TimeSlot.SLOT_7_00).getUserNames()).isEqualTo(Arrays.asList("원용", "소현"));
        assertThat(bestMeetingUtil.getTimeTable().get("7.12.수").get(TimeSlot.SLOT_7_00).getWeight()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 회의 시간 동안 가능한 회의 시간들을 모두 구한다.")
    public void collectAvailableMeetingTimeByDurationTest() {
        // given
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);
        UserDto userDto = new UserDto(1L, "심은서");
        UserDto userDto2 = new UserDto(2L, "이동헌");
        UserDto userDto3 = new UserDto(3L, "이재훈");
        List<UserDto> users = Arrays.asList(userDto, userDto2, userDto3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, "원용", 0);
        MeetingTimeDto meetingTimeDto2 = new MeetingTimeDto("7", "11", "화", TimeSlot.SLOT_12_00, TimeSlot.SLOT_14_00, "원용", 0);
        MeetingTimeDto meetingTimeDto3 = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_18_00, TimeSlot.SLOT_20_00, "소현", 0);
        MeetingTimeDto meetingTimeDto4 = new MeetingTimeDto("7", "11", "화", TimeSlot.SLOT_12_00, TimeSlot.SLOT_14_00, "소현", 0);
        List<MeetingTimeDto> meetingTimes = Arrays.asList(meetingTimeDto, meetingTimeDto2, meetingTimeDto3, meetingTimeDto4);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingDto);
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "initTimeTable");
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "setUserMeetingTime", meetingTimes);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto(
                "7.10.월",
                TimeSlot.SLOT_18_00,
                TimeSlot.SLOT_20_00,
                0,
                Arrays.asList("원용", "소현"),
                false
        );

        AvailableMeetingTimeDto result2 = new AvailableMeetingTimeDto(
                "7.11.화",
                TimeSlot.SLOT_12_00,
                TimeSlot.SLOT_14_00,
                0,
                Arrays.asList("원용", "소현"),
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
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);
        UserDto userDto = new UserDto(1L, "심은서");
        UserDto userDto2 = new UserDto(2L, "이동헌");
        UserDto userDto3 = new UserDto(3L, "이재훈");
        UserDto userDto4 = new UserDto(3L, "정찬우");
        List<UserDto> users = Arrays.asList(userDto, userDto2, userDto3, userDto4);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, HOUR, users);

        PossibleTimeCaseDto tc = new PossibleTimeCaseDto(HOUR, 4);
        PossibleTimeCaseDto tc2 = new PossibleTimeCaseDto(HALF, 4);
        PossibleTimeCaseDto tc3 = new PossibleTimeCaseDto(HOUR, 3);
        PossibleTimeCaseDto tc4 = new PossibleTimeCaseDto(HALF, 3);
        PossibleTimeCaseDto tc5 = new PossibleTimeCaseDto(HOUR, 2);
        PossibleTimeCaseDto tc6 = new PossibleTimeCaseDto(HALF, 2);
        PossibleTimeCaseDto tc7 = new PossibleTimeCaseDto(HOUR, 1);
        PossibleTimeCaseDto tc8 = new PossibleTimeCaseDto(HALF, 1);
        List<PossibleTimeCaseDto> timeCases = Arrays.asList(tc, tc2, tc3, tc4, tc5, tc6, tc7, tc8);

        ReflectionTestUtils.setField(bestMeetingUtil, "meeting", meetingDto);

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "getAllPossibleMeetingTimeCases", meetingDto.getDuration());

        // then
        // assertThat(timeCases).isEqualTo(bestMeetingUtil.getTimeCases());
    }

    @Test
    @DisplayName("최종 회의시간 도출하기")
    public void getBestMeetingTime() {
        // given
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);
        UserDto userDto = new UserDto(1L, "심은서");
        UserDto userDto2 = new UserDto(2L, "이동헌");
        UserDto userDto3 = new UserDto(3L, "이재훈");
        List<UserDto> users = Arrays.asList(userDto, userDto2, userDto3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        // 14~16 , 14:00 15:30 , 14:30 16:00
        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_12_00, TimeSlot.SLOT_20_00, "심은서", 0);
        MeetingTimeDto meetingTimeDto2 = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_12_00, TimeSlot.SLOT_16_00, "이동헌", 0);
        MeetingTimeDto meetingTimeDto3 = new MeetingTimeDto("7", "10", "월", TimeSlot.SLOT_14_00, TimeSlot.SLOT_16_00, "이재훈", 0);
        List<MeetingTimeDto> meetingTimes = Arrays.asList(meetingTimeDto, meetingTimeDto2, meetingTimeDto3);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto(
                "7.10.월",
                TimeSlot.SLOT_14_00,
                TimeSlot.SLOT_16_00,
                0,
                Arrays.asList("심은서", "이동헌", "이재훈"),
                true
        );

        AvailableMeetingTimeDto result2 = new AvailableMeetingTimeDto(
                "7.10.월",
                TimeSlot.SLOT_14_00,
                TimeSlot.SLOT_15_30,
                0,
                Arrays.asList("심은서", "이동헌", "이재훈"),
                true
        );
        AvailableMeetingTimeDto result3 = new AvailableMeetingTimeDto(
                "7.10.월",
                TimeSlot.SLOT_14_30,
                TimeSlot.SLOT_16_00,
                0,
                Arrays.asList("심은서", "이동헌", "이재훈"),
                true
        );

        // when
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);

        // then
        assertAll(
                () -> assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, result3))
        );
    }
}
