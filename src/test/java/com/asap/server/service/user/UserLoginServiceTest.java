package com.asap.server.service.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.HostTimeForbiddenException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.common.jwt.JwtService;
import com.asap.server.persistence.domain.ConfirmedDateTime;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.service.TimeBlockUserService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Mock
    private TimeBlockUserService timeBlockUserService;
    UserLoginService userLoginService;

    @BeforeEach
    void setUp() {
        userLoginService = new UserLoginService(
                meetingRepository,
                jwtService,
                passwordEncoder,
                timeBlockUserService
        );
    }

    @DisplayName("아직 확정되지 않은 특정 회의의 방장의 이름과 비밀번호가 일치하면 accessToken을 반환한다.")
    @Test
    void test() {
        // given
        long meetingId = 1L;
        String encodedPassword = passwordEncoder.encode("0000");
        Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password(encodedPassword)
                .build();
        Name name = new Name("KWY");
        User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name(name)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));
        when(timeBlockUserService.isEmptyHostTimeBlock(host)).thenReturn(false);
        when(jwtService.issuedToken("1")).thenReturn("access token");

        String expected = "access token";

        // when
        String response = userLoginService.loginByHost(meetingId, "KWY", "0000");

        // then
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("특정 회의의 방장 이름과 일치하지 않는다면 UnauthorizedException 에러를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"user1", "user2", "K"})
    void test2(String name) {
        // given
        long meetingId = 1L;
        String encodedPassword = passwordEncoder.encode("0000");
        final Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password(encodedPassword)
                .build();
        Name hostName = new Name("KWY");
        final User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name(hostName)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));

        // when, then
        assertThatThrownBy(() -> {
            userLoginService.loginByHost(meetingId, name, "0000");
        }).isInstanceOf(UnauthorizedException.class);
    }

    @DisplayName("특정 회의의 비밀번호가 일치하지 않는다면 UnauthorizedException 에러를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1111", "1112", "1234"})
    void test3(String password) {
        // given
        long meetingId = 1L;
        String encodedPassword = passwordEncoder.encode("0000");
        final Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password(encodedPassword)
                .build();
        Name hostName = new Name("KWY");
        final User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name(hostName)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));

        // when, then
        assertThatThrownBy(() -> {
            userLoginService.loginByHost(meetingId, "KWY", password);
        }).isInstanceOf(UnauthorizedException.class);
    }

    @DisplayName("이미 확정된 회의라면 ConflictException 에러를 반환한다.")
    @Test
    void test4() {
        // given
        long meetingId = 1L;
        String encodedPassword = passwordEncoder.encode("0000");
        final Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password(encodedPassword)
                .confirmedDateTime(new ConfirmedDateTime(LocalDateTime.now(), LocalDateTime.now()))
                .build();
        Name hostName = new Name("KWY");
        final User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name(hostName)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));

        // when, then
        assertThatThrownBy(() -> {
            userLoginService.loginByHost(meetingId, "KWY", "0000");
        }).isInstanceOf(ConflictException.class);
    }

    @DisplayName("방장이 시간을 입력하지 않았다면 HostTimeForbiddenException 에러를 반환한다.")
    @Test
    void test5() {
        // given
        long meetingId = 1L;
        String encodedPassword = passwordEncoder.encode("0000");
        final Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password(encodedPassword)
                .build();
        Name hostName = new Name("KWY");
        final User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name(hostName)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));
        when(jwtService.issuedToken("1")).thenReturn("access token");
        when(timeBlockUserService.isEmptyHostTimeBlock(host)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> {
            userLoginService.loginByHost(meetingId, "KWY", "0000");
        }).isInstanceOf(HostTimeForbiddenException.class);
    }
}