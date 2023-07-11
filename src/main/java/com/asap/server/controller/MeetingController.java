package com.asap.server.controller;

import com.asap.server.common.dto.ApiResponse;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.exception.Success;
import com.asap.server.service.MeetingService;
import com.asap.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final UserService userService;

    @PostMapping("")
    public ApiResponse create(@RequestBody @Valid MeetingSaveRequestDto meetingSaveRequestDto){
        String accessToken = userService.createHost(meetingSaveRequestDto.getName());
        String uri = meetingService.create(meetingSaveRequestDto);
        return ApiResponse.success(Success.CREATE_MEETING_SUCCESS, new MeetingSaveResponseDto(uri, accessToken));
    }
}
