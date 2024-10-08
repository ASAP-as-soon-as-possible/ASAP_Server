package com.asap.server.presentation.controller.user;

import com.asap.server.common.exception.Success;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.presentation.controller.dto.request.HostLoginRequestDto;
import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import com.asap.server.presentation.controller.user.docs.UserRegisterControllerDocs;
import com.asap.server.service.user.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRegisterController implements UserRegisterControllerDocs {
    private final UserLoginService userLoginService;

    @PostMapping("{meetingId}/host")
    @Override
    public SuccessResponse<HostLoginResponseDto> loginByHost(
            @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final HostLoginRequestDto requestDto
    ) {
        String hostAccessToken = userLoginService.loginByHost(
                meetingId,
                requestDto.name(),
                requestDto.password()
        );

        return SuccessResponse.success(
                Success.LOGIN_SUCCESS,
                new HostLoginResponseDto(hostAccessToken)
        );
    }
}
