package com.asap.server.common.utils;

import com.asap.server.controller.dto.response.DateAvailabilityDto;
import com.asap.server.controller.dto.response.MeetingDto;
import com.asap.server.domain.enums.Duration;
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
}
