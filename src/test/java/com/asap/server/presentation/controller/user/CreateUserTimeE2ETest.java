package com.asap.server.presentation.controller.user;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asap.server.common.jwt.JwtService;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.Place;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.time.UserMeetingSchedule;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.presentation.common.secure.SecureUrlUtil;
import com.asap.server.presentation.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.presentation.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CreateUserTimeE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EntityManager em;
    @Autowired
    private SecureUrlUtil secureUrlUtil;
    @Autowired
    private JwtService jwtService;

    @Nested
    @DisplayName("[멤버] 시간 입력 테스트 코드")
    class MemberCreateUserTimeTest {
        @Test
        @DisplayName("[성공 케이스] 멤버가 회의 시간을 성공적으로 등록할 수 있다.")
        void test() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_15_00, TimeSlot.SLOT_16_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto3 = new UserMeetingTimeSaveRequestDto("7", "10",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2,
                    userMeetingTimeSaveRequestDto3
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);
            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());

            AvailableTimeRequestDto availableTimeRequestDto = new AvailableTimeRequestDto("KWY", availableTimes);
            String body = objectMapper.writeValueAsString(availableTimeRequestDto);

            // when, then
            mockMvc.perform(
                            post("/user/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    ).andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.message").value("참여자 회의 가능 시간 입력을 성공하였습니다."))
                    .andExpect(jsonPath("$.data.role").value("MEMBER"));

            String jpql = "SELECT ums FROM UserMeetingSchedule ums WHERE ums.meetingId=" + meeting.getId();
            List<UserMeetingSchedule> result =
                    em.createQuery(jpql, UserMeetingSchedule.class).getResultList();

            assertThat(result.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("[오류 케이스] 멤버가 특정 요일에 중복된 시간을 입력하면 400 에러를 반환한다.")
        void test2() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_13_00, TimeSlot.SLOT_16_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);
            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());

            AvailableTimeRequestDto availableTimeRequestDto = new AvailableTimeRequestDto("KWY", availableTimes);
            String body = objectMapper.writeValueAsString(availableTimeRequestDto);

            // when, then
            mockMvc.perform(
                            post("/user/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("중복 입력된 시간이 있습니다."));
        }
    }

    @Nested
    @DisplayName("[방장] 시간 입력 테스트")
    class HostCreateUserTimeTest {
        @Test
        @DisplayName("[성공 케이스] 방장이 회의 시간을 성공적으로 등록할 수 있다.")
        void test() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_15_00, TimeSlot.SLOT_16_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto3 = new UserMeetingTimeSaveRequestDto("7", "10",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2,
                    userMeetingTimeSaveRequestDto3
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);

            User user = User.builder()
                    .name(new Name("KWY"))
                    .meeting(meeting)
                    .role(Role.HOST)
                    .build();
            em.persist(user);
            meeting.setHost(user);

            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());
            String hostJwtToken = jwtService.issuedToken(String.valueOf(user.getId()));

            String body = objectMapper.writeValueAsString(availableTimes);

            // when, then
            mockMvc.perform(
                            post("/user/host/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .header("Authorization", "Bearer " + hostJwtToken)
                    ).andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.message").value("방장의 회의 가능 시간이 성공적으로 입력되었습니다."));

            String jpql = "SELECT ums FROM UserMeetingSchedule ums WHERE ums.meetingId=" + meeting.getId();
            List<UserMeetingSchedule> result =
                    em.createQuery(jpql, UserMeetingSchedule.class).getResultList();

            assertThat(result.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("[오류 케이스] 방장이 특정 요일에 중복된 시간을 입력하면 400 에러를 반환한다.")
        void test2() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_13_00, TimeSlot.SLOT_16_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);

            User user = User.builder()
                    .name(new Name("KWY"))
                    .meeting(meeting)
                    .role(Role.HOST)
                    .build();
            em.persist(user);
            meeting.setHost(user);

            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());
            String hostJwtToken = jwtService.issuedToken(String.valueOf(user.getId()));

            String body = objectMapper.writeValueAsString(availableTimes);

            // when, then
            mockMvc.perform(
                            post("/user/host/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .header("Authorization", "Bearer " + hostJwtToken)
                    ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("중복 입력된 시간이 있습니다."));
        }

        @Test
        @DisplayName("[오류 케이스] 요청자가 방장이 아닐 경우 401 에러를 반환한다.")
        void test3() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_13_00, TimeSlot.SLOT_16_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);

            User user = User.builder()
                    .name(new Name("KWY"))
                    .meeting(meeting)
                    .role(Role.HOST)
                    .build();
            em.persist(user);
            meeting.setHost(user);

            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());
            String hostJwtToken = jwtService.issuedToken(String.valueOf(user.getId() + 1));

            String body = objectMapper.writeValueAsString(availableTimes);

            // when, then
            mockMvc.perform(
                            post("/user/host/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .header("Authorization", "Bearer " + hostJwtToken)
                    ).andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("해당 유저는 해당 방의 방장이 아닙니다."));
        }

        @Test
        @DisplayName("[오류 케이스] 방장이 이미 시간을 입력했을 경우, 추가 입력 요청 시 409 에러를 반환한다.")
        void test4() throws Exception {
            // given
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_9_00, TimeSlot.SLOT_14_00, 1);
            UserMeetingTimeSaveRequestDto userMeetingTimeSaveRequestDto2 = new UserMeetingTimeSaveRequestDto("7", "9",
                    TimeSlot.SLOT_13_00, TimeSlot.SLOT_16_00, 1);
            List<UserMeetingTimeSaveRequestDto> availableTimes = List.of(
                    userMeetingTimeSaveRequestDto,
                    userMeetingTimeSaveRequestDto2
            );

            Place place = Place.builder()
                    .placeType(PlaceType.OFFLINE)
                    .build();
            Meeting meeting = Meeting.builder()
                    .title("회의 테스트")
                    .password("0000")
                    .additionalInfo("")
                    .duration(Duration.HALF)
                    .place(place)
                    .build();
            em.persist(meeting);

            User user = User.builder()
                    .name(new Name("KWY"))
                    .meeting(meeting)
                    .role(Role.HOST)
                    .build();
            em.persist(user);
            meeting.setHost(user);

            UserMeetingSchedule userMeetingSchedule = UserMeetingSchedule.builder()
                    .meetingId(meeting.getId())
                    .userId(user.getId())
                    .availableDate(LocalDate.of(2024, 7, 9))
                    .weight(0)
                    .startTimeSlot(TimeSlot.SLOT_11_00)
                    .endTimeSlot(TimeSlot.SLOT_16_30)
                    .build();

            em.persist(userMeetingSchedule);

            String encodedMeetingId = secureUrlUtil.encodeUrl(meeting.getId());
            String hostJwtToken = jwtService.issuedToken(String.valueOf(user.getId()));

            String body = objectMapper.writeValueAsString(availableTimes);

            // when, then
            mockMvc.perform(
                            post("/user/host/" + encodedMeetingId + "/time")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .header("Authorization", "Bearer " + hostJwtToken)
                    ).andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("이미 가능 시간 입력을 마쳤습니다."));
        }
    }
}
