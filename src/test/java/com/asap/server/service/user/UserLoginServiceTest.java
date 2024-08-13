package com.asap.server.service.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.asap.server.common.jwt.JwtService;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.User;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import com.asap.server.service.TimeBlockUserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TimeBlockUserService timeBlockUserService;
    @InjectMocks
    UserLoginService userLoginService;

    @Test
    @DisplayName("아직 확정되지 않은 특정 회의의 방장의 이름과 비밀번호가 일치하면 accessToken을 반환한다.")
    void test() {
        // given
        long meetingId = 1L;
        final Meeting meeting = Meeting.builder()
                .id(meetingId)
                .password("0000")
                .build();
        final User host = User.builder()
                .id(1L)
                .meeting(meeting)
                .name("KWY")
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(host);
        when(meetingRepository.findByIdWithHost(meetingId)).thenReturn(Optional.of(meeting));
        when(passwordEncoder.matches(meeting.getPassword(), "0000")).thenReturn(true);
        when(timeBlockUserService.isEmptyHostTimeBlock(host)).thenReturn(false);
        when(jwtService.issuedToken("1")).thenReturn("access token");
        
        HostLoginResponseDto expected = new HostLoginResponseDto("access token");

        // when
        HostLoginResponseDto response = userLoginService.loginByHost(meetingId, "KWY", "0000");

        // then
        assertThat(response).isEqualTo(expected);
    }
}