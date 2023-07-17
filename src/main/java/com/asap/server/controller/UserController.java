package com.asap.server.controller;

import com.asap.server.common.dto.ApiResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.exception.Success;
import com.asap.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/host/{meetingId}/time")
    public ApiResponse createHostTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody List<@Valid UserMeetingTimeSaveRequestDto> requestDtoList,
            @UserId Long userId,
            @MeetingId Long meetingId
    ) {
        return ApiResponse.success(Success.CREATE_HOST_TIME_SUCCESS, userService.createHostTime(meetingId, _meetingId, userId, requestDtoList));
    }

    @PostMapping("/{meetingId}/time")
    public ApiResponse createMemberTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid AvailableTimeRequestDto requestDto,
            @MeetingId Long meetingId
    ) {
        return ApiResponse.success(Success.CREATE_MEETING_TIME_SUCCESS, userService.createMemberMeetingTime(meetingId, requestDto));
    }

    @PostMapping("{meetingId}/host")
    public ApiResponse loginByHost(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid HostLoginRequestDto requestDto,
            @MeetingId Long meetingId
    ) {
        return ApiResponse.success(Success.LOGIN_SUCCESS, userService.loginByHost(meetingId, requestDto));
    }
}
