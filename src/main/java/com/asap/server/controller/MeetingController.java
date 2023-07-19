package com.asap.server.controller;

import com.asap.server.common.dto.SuccessResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.exception.Success;
import com.asap.server.service.MeetingService;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회의", description = "회의 관련 API 입니다.")
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @Operation(summary = "[회의 생성 뷰] 회의 생성 API")
    @PostMapping
    public SuccessResponse<MeetingSaveResponseDto> create(
            @RequestBody @Valid MeetingSaveRequestDto meetingSaveRequestDto
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_SUCCESS, meetingService.create(meetingSaveRequestDto));
    }

    @PostMapping("/{meetingId}/confirm")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse confirmMeeting(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid MeetingConfirmRequestDto meetingConfirmRequestDto,
            @MeetingId Long meetingId,
            @UserId Long userId
    ) {
        meetingService.confirmMeeting(meetingConfirmRequestDto, meetingId, userId);
        return SuccessResponse.success(Success.CONFIRM_MEETING_SUCCESS);
    }

    @Operation(summary = "[가능 시간 입력 뷰] 회의 선택 시간표 제공 API")
    @GetMapping("/{meetingId}/schedule")
    public SuccessResponse getMeetingSchedule(
            @PathVariable("meetingId") String _meetingId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.FIND_MEETING_SCHEDULE_SUCCESS, meetingService.getMeetingSchedule(meetingId));
    }

    @GetMapping("/{meetingId}/card")
    public SuccessResponse<FixedMeetingResponseDto> getFixedMeetingInformation(
            @PathVariable("meetingId") String _meetingId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.FIXED_MEETING_SUCCESS, meetingService.getFixedMeetingInformation(meetingId));
    }

    @Operation(summary = "[방장 뷰] 종합 일정 시간표 제공 API")
    @GetMapping("/{meetingId}/timetable")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<TimeTableResponseDto> getTimeTable(
            @PathVariable("meetingId") String _meetingId,
            @UserId Long userId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.FIND_TIME_TABLE_SUCCESS, meetingService.getTimeTable(userId, meetingId));
    }

    @Operation(summary = "[회의 입장 뷰] 회의 유효성 체크 API")
    @GetMapping("/{meetingId}")
    public SuccessResponse getIsFixedMeeting(
            @PathVariable("meetingId") String _meetingId,
            @UserId Long userId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.MEETING_VALIDATION_SUCCESS, meetingService.getIsFixedMeeting(userId, meetingId));
    }
}
