package com.asap.server.controller;

import com.asap.server.common.dto.ErrorResponse;
import com.asap.server.common.dto.SuccessResponse;
import com.asap.server.config.resolver.meeting.MeetingId;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.exception.Success;
import com.asap.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유효한 회의 입니다."),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/host/{meetingId}/time")
    public SuccessResponse createHostTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody List<@Valid UserMeetingTimeSaveRequestDto> requestDtoList,
            @UserId @Parameter(hidden = true) Long userId,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.CREATE_HOST_TIME_SUCCESS, userService.createHostTime(meetingId, _meetingId, userId, requestDtoList));
    }

    @Operation(summary = "[회의 가능 시간 입력 뷰 - 참여자] 참여자 정보 및 가능 시간 입력 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 선택지가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{meetingId}/time")
    public SuccessResponse createMemberTime(
            @PathVariable("meetingId") String _meetingId,
            @RequestBody @Valid AvailableTimeRequestDto requestDto,
            @MeetingId Long meetingId
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_TIME_SUCCESS, userService.createMemberMeetingTime(meetingId, requestDto));
    }

    @Operation(summary = "[방장 입장 뷰] 방장 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 선택지가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 사용자 이름 또는 비밀번호입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("{meetingId}/host")
    public SuccessResponse loginByHost(
            @PathVariable("meetingId") final String _meetingId,
            @RequestBody @Valid final HostLoginRequestDto requestDto,
            @MeetingId final Long meetingId
    ) {
        return SuccessResponse.success(Success.LOGIN_SUCCESS, userService.loginByHost(meetingId, requestDto));
    }
}
