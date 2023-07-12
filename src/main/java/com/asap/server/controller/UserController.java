package com.asap.server.controller;

import com.asap.server.common.dto.ApiResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.exception.Success;
import com.asap.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/host/{meetingId}/time")
    public ApiResponse createHostTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid List<UserMeetingTimeSaveRequestDto> requestDtoList,
            @MeetingId Long meetingId){
        return ApiResponse.success(Success.CREATE_HOST_TIME_SUCCESS, userService.createHostTime(_meetingId, meetingId, requestDtoList));
    }
}
