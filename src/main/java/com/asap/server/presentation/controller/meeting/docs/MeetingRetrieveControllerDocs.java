package com.asap.server.presentation.controller.meeting.docs;

import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.presentation.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.presentation.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.presentation.controller.dto.response.MeetingTitleResponseDto;
import com.asap.server.presentation.controller.dto.response.TimeTableResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회의", description = "회의 관련 API 입니다.")
public interface MeetingRetrieveControllerDocs {
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
    SuccessResponse<MeetingScheduleResponseDto> getMeetingSchedule(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId
    );

    @Operation(summary = "[큐 카드] 큐 카드 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "큐카드 조회 성공입니다."),
            @ApiResponse(responseCode = "400", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "확정되지 않은 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    SuccessResponse<FixedMeetingResponseDto> getFixedMeetingInformation(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId
    );

    @Operation(summary = "[방장 뷰] 종합 일정 시간표 제공 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "종합 일정 시간표 조회 성공입니다."),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description =
                            "1. 해당 회의는 존재하지 않습니다.\n"
                                    + "2. 해당 유저는 존재하지 않습니다.\n"
                                    + "3. 회의 가능 일자가 존재하지 않습니다.\n"
                                    + "4. 해당 회의의 가능 시간을 입력한 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "JWT Auth")
    SuccessResponse<TimeTableResponseDto> getTimeTable(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            @Parameter(hidden = true) final Long userId
    );

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
    SuccessResponse<MeetingTitleResponseDto> getIsFixedMeeting(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId
    );

    @Operation(summary = "[회의 일정 확정 뷰] 최적의 회의 시간 확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최적의 회의시간 조회 성공입니다."),
            @ApiResponse(responseCode = "401", description = "해당 유저는 해당 방의 방장이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "1. 해당 회의는 존재하지 않습니다.\n"
                            + "2. 회의 가능 일자가 존재하지 않습니다."
                    , content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "JWT Auth")
    SuccessResponse<BestMeetingTimeResponseDto> getBestMeetingTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            @Parameter(hidden = true) Long userId
    );
}
