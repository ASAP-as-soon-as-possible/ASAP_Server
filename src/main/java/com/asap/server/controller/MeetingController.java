package com.asap.server.controller;

import com.asap.server.common.dto.ErrorResponse;
import com.asap.server.common.dto.SuccessResponse;
import com.asap.server.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.config.resolver.user.UserId;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingTitleResponseDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.exception.Success;
import com.asap.server.service.MeetingService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "회의", description = "회의 관련 API 입니다.")
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @Operation(summary = "[회의 생성 뷰] 회의 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회의가 성공적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400",
                    description = "1. 요청값이 유효하지 않습니다.\n"
                            + "2. 입력 형식이 맞지 않습니다.\n"
                            + "3. 제목의 최대 입력 길이(15자)를 초과했습니다.\n"
                            + "4. 회의 가능 날짜 형식은 YYYY/mm/dd/ddd 입니다.\n"
                            + "5. 회의 형식이 입력되지 않았습니다.\n"
                            + "6. 회의 진행 시간이 입력되지 않았습니다.\n"
                            + "7. 방장의 이름이 입력되지 않았습니다."
                            + "8. 방장 이름의 최대 입력 길이(8자)를 초과했습니다.\n"
                            + "9. 회의 비밀번호가 입력되지 않았습니다.\n"
                            + "10. 비밀번호의 최소 입력 길이는 4자입니다.\n"
                            + "11. 비밀번호는 4자리 이상 숫자입니다.\n"
                            + "12. 추가 내용의 최대 입력 길이(50자)를 초과했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public SuccessResponse<MeetingSaveResponseDto> create(
            @RequestBody @Valid final MeetingSaveRequestDto meetingSaveRequestDto
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_SUCCESS, meetingService.create(meetingSaveRequestDto));
    }

    @Operation(summary = "[큐 카드] 큐 카드 확정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 회의 진행 월이 입력되지 않았습니다.\n"
                            + "2. 회의 진행 날짜가 입력되지 않았습니다.\n"
                            + "3. 회의 진행 요일이 입력되지 않았습니다.\n"
                            + "4. 회의 시작 시간이 입력되지 않았습니다.\n"
                            + "5. 회의 종료 시간이 입력되지 않았습니다.\n"
                            + "6. 회의 진행 시간이 입력되지 않았습니다.\n"
                            + "7. 해당 유저는 해당 방의 방장이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{meetingId}/confirm")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse confirmMeeting(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final MeetingConfirmRequestDto meetingConfirmRequestDto,
            @UserId @Parameter(hidden = true) final Long userId
    ) {
        meetingService.confirmMeeting(meetingConfirmRequestDto, meetingId, userId);
        return SuccessResponse.success(Success.CONFIRM_MEETING_SUCCESS);
    }

    @Operation(summary = "[가능 시간 입력 뷰] 회의 선택 시간표 제공 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 선택지가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "404",
                    description = "1. 해당 회의는 존재하지 않습니다.\n"
                            + "2. 회의 가능 일자가 존재하지 않습니다.\n"
                            + "3. 회의 선호 시간대가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{meetingId}/schedule")
    public SuccessResponse getMeetingSchedule(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(Success.FIND_MEETING_SCHEDULE_SUCCESS, meetingService.getMeetingSchedule(meetingId));
    }

    @Operation(summary = "[큐 카드] 큐 카드 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "확정되지 않은 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{meetingId}/card")
    public SuccessResponse<FixedMeetingResponseDto> getFixedMeetingInformation(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(Success.FIXED_MEETING_SUCCESS, meetingService.getFixedMeetingInformation(meetingId));
    }

    @Operation(summary = "[방장 뷰] 종합 일정 시간표 제공 API")
    @GetMapping("/{meetingId}/timetable")
    @SecurityRequirement(name = "JWT Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 선택지가 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description =
                            "1. 해당 회의는 존재하지 않습니다.\n"
                                    + "2. 해당 유저는 존재하지 않습니다.\n"
                                    + "3. 회의 가능 일자가 존재하지 않습니다.\n"
                                    + "4. 해당 회의의 가능 시간을 입력한 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<TimeTableResponseDto> getTimeTable(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @UserId @Parameter(hidden = true) final Long userId
    ) {
        return SuccessResponse.success(Success.FIND_TIME_TABLE_SUCCESS, meetingService.getTimeTable(userId, meetingId));
    }

    @Operation(summary = "[회의 입장 뷰] 회의 유효성 체크 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유효한 회의 입니다."),
            @ApiResponse(responseCode = "404",
                    description = "1. 해당 회의는 존재하지 않습니다\n."
                            + "2. 해당 유저는 존재하지 않습니다.\n"
                            + "3. 회의 가능 일자가 존재하지 않습니다."
                    , content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{meetingId}")
    public SuccessResponse<MeetingTitleResponseDto> getIsFixedMeeting(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(Success.MEETING_VALIDATION_SUCCESS, meetingService.getIsFixedMeeting(meetingId));
    }

    @Operation(summary = "[회의 일정 확정 뷰] 최적의 회의 시간 확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1. 해당 회의는 존재하지 않습니다.\n"
                            + "2. 회의 가능 일자가 존재하지 않습니다."
                    , content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{meetingId}/details")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<BestMeetingTimeResponseDto> getBestMeetingTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) @MeetingPathVariable final Long meetingId,
            @Parameter(hidden = true) @UserId Long userId
    ) {
        return SuccessResponse.success(Success.BEST_MEETING_SUCCESS, meetingService.getBestMeetingTime(meetingId, userId));
    }
}
