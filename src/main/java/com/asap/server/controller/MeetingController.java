package com.asap.server.controller;

import com.asap.server.common.dto.ApiResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.exception.Success;
import com.asap.server.service.MeetingService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public ApiResponse<MeetingSaveResponseDto> create(
            @RequestBody @Valid MeetingSaveRequestDto meetingSaveRequestDto
    ) {
        return ApiResponse.success(Success.CREATE_MEETING_SUCCESS, meetingService.create(meetingSaveRequestDto));
    }

    @PostMapping("/{meetingId}/confirm")
    public ApiResponse confirmMeeting(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid MeetingConfirmRequestDto meetingConfirmRequestDto,
            @MeetingId Long meetingId,
            @UserId Long userId
    ) {
        meetingService.confirmMeeting(meetingConfirmRequestDto, meetingId, userId);
        return ApiResponse.success(Success.CONFIRM_MEETING_SUCCESS);
    }

    @GetMapping("/host/{meetingId}")
    public ApiResponse checkRightHost(
            @PathVariable("meetingId") String _meetingId,
            @UserId Long userId,
            @MeetingId Long meetingId
    ){
        meetingService.checkRightHost(meetingId, userId);
        return ApiResponse.success(Success.USER_MATCH_SUCCESS);
    }
}
