package com.asap.server.controller;

import com.asap.server.common.dto.ErrorDataResponse;
import com.asap.server.common.dto.ErrorResponse;
import com.asap.server.common.dto.SuccessResponse;
import com.asap.server.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.controller.dto.response.HostLoginResponseDto;
import com.asap.server.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.UserTimeResponseDto;
import com.asap.server.exception.Success;
import com.asap.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
            @ApiResponse(responseCode = "200", description = "방장의 회의 가능 시간이 성공적으로 입력되었습니다."),
            @ApiResponse(responseCode = "400",
                    description = "1. 시간 형식이 잘못되었습니다. [YYYY/MM/DD HH:MM]\n"
                            + "2. 중복 입력된 시간이 있습니다.\n"
                            + "3. 입력한 시간이 회의 가능 일시에 해당하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "1. 방장의 토큰이 필요합니다.\n"
                            + "2. 토큰이 유효하지 않습니다.\n"
                            + "3. 해당 유저는 해당 방의 방장이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "해당 회의 방장의 가능시간이 이미 존재합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/host/{meetingId}/time")
    public SuccessResponse<UserMeetingTimeResponseDto> createHostTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @RequestBody final List<@Valid @NotNull UserMeetingTimeSaveRequestDto> requestDtoList,
            @UserId @Parameter(hidden = true) final Long userId
    ) {
        return SuccessResponse.success(Success.CREATE_HOST_TIME_SUCCESS, userService.createHostTime(meetingId, userId, requestDtoList));
    }

    @Operation(summary = "[회의 가능 시간 입력 뷰 - 참여자] 참여자 정보 및 가능 시간 입력 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여자 회의 가능 시간 입력을 성공하였습니다."),
            @ApiResponse(responseCode = "400",
                    description = "1. 시간 형식이 잘못되었습니다. [YYYY/MM/DD HH:MM]\n"
                            + "2. 중복 입력된 시간이 있습니다.\n"
                            + "3. 입력한 시간이 회의 가능 일시에 해당하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{meetingId}/time")
    public SuccessResponse<UserTimeResponseDto> createMemberTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final AvailableTimeRequestDto requestDto
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_TIME_SUCCESS, userService.createUserTime(meetingId, requestDto));
    }

    @Operation(summary = "[방장 입장 뷰] 방장 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공입니다"),
            @ApiResponse(responseCode = "400",
                    description = "1. 방장 이름의 최대 입력 길이(8자)를 초과했습니다.\n"
                            + "2. 비밀번호는 4자리 이상 숫자입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 사용자 이름 또는 비밀번호입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "회의 가능 시간이 입력되지 않았습니다.", content = @Content(schema = @Schema(implementation = ErrorDataResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("{meetingId}/host")
    public SuccessResponse<HostLoginResponseDto> loginByHost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final HostLoginRequestDto requestDto
    ) {
        return SuccessResponse.success(Success.LOGIN_SUCCESS, userService.loginByHost(meetingId, requestDto));
    }
}
