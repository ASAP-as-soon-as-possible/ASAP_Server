package com.asap.server.service.meeting;

import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.UserRequestDto;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.Place;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.PlaceType;
import com.asap.server.domain.enums.Role;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.service.MeetingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ConfirmMeetingMethodTest {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("회의 확정시 ConfirmedDateTime 은 Update 된다.")
    void setConfirmDateTimeTest() {
        // given
        final Place place = Place.builder()
                .placeType(PlaceType.OFFLINE)
                .build();
        final Meeting meeting = Meeting.builder()
                .title("회의 테스트")
                .password("0000")
                .additionalInfo("")
                .duration(Duration.HALF)
                .place(place)
                .build();
        final User user = User.builder()
                .meeting(meeting)
                .name("강원용")
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(user);

        em.persist(meeting);
        em.persist(user);
        em.flush();
        em.clear();

        final UserRequestDto userDto = UserRequestDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
        final MeetingConfirmRequestDto body = MeetingConfirmRequestDto.builder()
                .month("09")
                .day("07")
                .dayOfWeek("월")
                .startTime(TimeSlot.SLOT_6_00)
                .endTime(TimeSlot.SLOT_6_30)
                .users(List.of(userDto))
                .build();

        // when
        meetingService.confirmMeeting(body, meeting.getId(), user.getId());

        // then
        final Meeting result = meetingRepository.findById(meeting.getId()).get();
        assertThat(result.isConfirmedMeeting()).isTrue();
    }

}
