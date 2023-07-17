package com.asap.server.controller;

import com.asap.server.common.dto.SuccessResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.exception.Success;
import com.asap.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "사용자", description = "사용자 관련 로그인 및 가능 시간 입력 API 입니다.")
@RestController
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "[회의 가능 시간 입력 뷰 - 방장] 방장 가능 시간 입력 API")
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping("/host/{meetingId}/time")
    public SuccessResponse createHostTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody List<@Valid UserMeetingTimeSaveRequestDto> requestDtoList,
            @UserId Long userId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.CREATE_HOST_TIME_SUCCESS, userService.createHostTime(meetingId, _meetingId, userId, requestDtoList));
    }

    @Operation(summary = "[회의 가능 시간 입력 뷰 - 참여자] 참여자 정보 및 가능 시간 입력 API")
    @PostMapping("/{meetingId}/time")
    public SuccessResponse createMemberTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid AvailableTimeRequestDto requestDto,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_TIME_SUCCESS, userService.createMemberMeetingTime(meetingId, requestDto));
    }

    @Operation(summary = "[방장 입장 뷰] 방장 로그인 API")
    @PostMapping("{meetingId}/host")
    public SuccessResponse loginByHost(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid HostLoginRequestDto requestDto,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.LOGIN_SUCCESS, userService.loginByHost(meetingId, requestDto));
    }
}
