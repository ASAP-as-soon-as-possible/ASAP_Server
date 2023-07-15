package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableMeetingTimeDto;
import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.TimeSlot;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR);

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
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2, dateAvailability3);
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR);

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
        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR);

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
                Arrays.asList("원용", "소현")
        );

        AvailableMeetingTimeDto result2 = new AvailableMeetingTimeDto(
                "7.11.화",
                TimeSlot.SLOT_12_00,
                TimeSlot.SLOT_14_00,
                0,
                Arrays.asList("원용", "소현")
        );

        // when
        ReflectionTestUtils.invokeMethod(bestMeetingUtil, "collectAvailableMeetingTimeByDuration", Duration.TWO_HOUR);

        // then
        assertThat(bestMeetingUtil.getAvailableMeetingTimesByDuration().get(Duration.TWO_HOUR)).isEqualTo(Arrays.asList(result, result2));
    }
}
