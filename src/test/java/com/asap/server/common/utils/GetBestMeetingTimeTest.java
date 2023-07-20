package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.AvailableMeetingTimeDto;
import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.controller.dto.response.MeetingTimeDto;
import com.asap.server.controller.dto.response.UserDto;
import com.asap.server.domain.enums.Duration;
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
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);

        UserDto userDto = new UserDto(1L, "강원용");
        List<UserDto> users = List.of(userDto);

        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", SLOT_12_00, SLOT_12_30, userDto, 0);
        List<MeetingTimeDto> meetingTimes = List.of(meetingTimeDto);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto("7.10.월", SLOT_12_00, SLOT_12_30, 0, List.of(userDto), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, null, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 2개만 있는 경우")
    public void getBestMeetingTime2() {
        // given
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);

        UserDto userDto = new UserDto(1L, "강원용");
        List<UserDto> users = List.of(userDto);

        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.HALF, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", SLOT_12_00, SLOT_13_00, userDto, 0);
        List<MeetingTimeDto> meetingTimes = List.of(meetingTimeDto);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto("7.10.월", SLOT_12_00, SLOT_12_30, 0, List.of(userDto), true);
        AvailableMeetingTimeDto result2 = new AvailableMeetingTimeDto("7.10.월", SLOT_12_30, SLOT_13_00, 0, List.of(userDto), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, null));
    }

    @Test
    @DisplayName("최적의 회의시간이 3개만 있는 경우")
    public void getBestMeetingTime3() {
        // given
        DateAvailabilityDto dateAvailability1 = new DateAvailabilityDto("7", "10", "월");
        DateAvailabilityDto dateAvailability2 = new DateAvailabilityDto("7", "11", "화");
        List<DateAvailabilityDto> dateAvailabilityDto = Arrays.asList(dateAvailability1, dateAvailability2);

        UserDto userDto = new UserDto(1L, "강원용");
        UserDto userDto2 = new UserDto(2L, "도소현");
        List<UserDto> users = Arrays.asList(userDto, userDto2);

        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", SLOT_12_00, SLOT_18_00, userDto, 0);
        MeetingTimeDto meetingTimeDto2 = new MeetingTimeDto("7", "11", "화", SLOT_12_00, SLOT_18_00, userDto2, 0);
        List<MeetingTimeDto> meetingTimes = Arrays.asList(meetingTimeDto, meetingTimeDto2);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto("7.10.월", SLOT_12_00, SLOT_14_00, 0, List.of(userDto), true);
        AvailableMeetingTimeDto result2 = new AvailableMeetingTimeDto("7.10.월", SLOT_12_30, SLOT_14_30, 0, List.of(userDto), true);
        AvailableMeetingTimeDto result3 = new AvailableMeetingTimeDto("7.10.월", SLOT_13_00, SLOT_15_00, 0, List.of(userDto), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, result2, result3));
    }
}
