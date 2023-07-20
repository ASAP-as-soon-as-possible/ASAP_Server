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

        MeetingDto meetingDto = new MeetingDto(dateAvailabilityDto, Duration.TWO_HOUR, users);

        MeetingTimeDto meetingTimeDto = new MeetingTimeDto("7", "10", "월", SLOT_12_00, SLOT_13_00, userDto, 0);
        List<MeetingTimeDto> meetingTimes = List.of(meetingTimeDto);

        AvailableMeetingTimeDto result = new AvailableMeetingTimeDto("7.10.월", SLOT_12_00, SLOT_12_30, 0, List.of(userDto), true);

        // when
        bestMeetingUtil.getBestMeetingTime(meetingDto, meetingTimes);

        // then
        assertThat(bestMeetingUtil.getFixedMeetingTime()).isEqualTo(Arrays.asList(result, null, null));
    }
}
