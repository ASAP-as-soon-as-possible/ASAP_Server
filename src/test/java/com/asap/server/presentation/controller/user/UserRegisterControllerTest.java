package com.asap.server.presentation.controller.user;

import static com.asap.server.common.exception.Error.HOST_MEETING_TIME_NOT_PROVIDED;
import static com.asap.server.common.exception.Error.INVALID_HOST_ID_PASSWORD_EXCEPTION;
import static com.asap.server.common.exception.Error.MEETING_VALIDATION_FAILED_EXCEPTION;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.HostTimeForbiddenException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.presentation.controller.dto.request.HostLoginRequestDto;
import com.asap.server.service.user.UserLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserLoginService userLoginService;

    @DisplayName("아직 확정되지 않은 특정 회의의 방장의 이름과 비밀번호가 일치하면 200 응답과 accessToken을 반환한다.")
    @Test
    void test() throws Exception {
        // given
        String encodedMeetingId = "MQ==";
        HostLoginRequestDto bodyDto = new HostLoginRequestDto("KWY", "0000");
        String body = objectMapper.writeValueAsString(bodyDto);
        when(userLoginService.loginByHost(1L, "KWY", "0000")).thenReturn("access token");

        mockMvc.perform(
                        post("/user/" + encodedMeetingId + "/host")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공입니다"))
                .andExpect(jsonPath("$.data.accessToken").value("access token"));
    }

    @DisplayName("방장 이름 또는 특정 회의의 비밀번호가 일치하지 않는다면 401 에러를 반환한다.")
    @Test
    void test2() throws Exception {
        // given
        String encodedMeetingId = "MQ==";
        HostLoginRequestDto bodyDto = new HostLoginRequestDto("USER1", "0000");
        String body = objectMapper.writeValueAsString(bodyDto);
        when(userLoginService.loginByHost(1L, "USER1", "0000"))
                .thenThrow(new UnauthorizedException(INVALID_HOST_ID_PASSWORD_EXCEPTION));

        mockMvc.perform(
                        post("/user/" + encodedMeetingId + "/host")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("유효하지 않은 사용자 이름 또는 비밀번호입니다."));
    }

    @DisplayName("이미 확정된 회의라면 409 에러를 반환한다.")
    @Test
    void test3() throws Exception {
        // given
        String encodedMeetingId = "MQ==";
        HostLoginRequestDto bodyDto = new HostLoginRequestDto("KWY", "0000");
        String body = objectMapper.writeValueAsString(bodyDto);
        when(userLoginService.loginByHost(1L, "KWY", "0000"))
                .thenThrow(new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION));

        mockMvc.perform(
                        post("/user/" + encodedMeetingId + "/host")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 확정된 회의입니다."));
    }

    @DisplayName("방장이 시간을 입력하지 않았다면 403 에러와 함께 jwt 토큰을 반환한다.")
    @Test
    void test4() throws Exception {
        // given
        String encodedMeetingId = "MQ==";
        HostLoginRequestDto bodyDto = new HostLoginRequestDto("KWY", "0000");
        String body = objectMapper.writeValueAsString(bodyDto);
        when(userLoginService.loginByHost(1L, "KWY", "0000"))
                .thenThrow(new HostTimeForbiddenException(HOST_MEETING_TIME_NOT_PROVIDED, "access token"));

        mockMvc.perform(
                        post("/user/" + encodedMeetingId + "/host")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("회의 가능 시간이 입력되지 않았습니다."))
                .andExpect(jsonPath("$.data.accessToken").value("access token"));
    }
}