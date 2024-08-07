package com.asap.server.presentation.controller.time.docs;

import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.presentation.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.presentation.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.presentation.controller.dto.response.UserTimeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "시간 입력 API", description = "방장 또는 참여자의 회의 가능한 시간을 입력하는 API")
public interface TimeRegisterControllerDocs {
    @Operation(summary = "[회의 가능 시간 입력 뷰 - 방장] 방장 가능 시간 입력 API")
    @SecurityRequirement(name = "JWT Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "방장의 회의 가능 시간이 성공적으로 입력되었습니다."),
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
    SuccessResponse<UserMeetingTimeResponseDto> createHostTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            final List<UserMeetingTimeSaveRequestDto> requestDtoList,
            @Parameter(hidden = true) final Long userId
    );

    @Operation(summary = "[회의 가능 시간 입력 뷰 - 참여자] 참여자 정보 및 가능 시간 입력 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "참여자 회의 가능 시간 입력을 성공하였습니다."),
            @ApiResponse(responseCode = "400",
                    description = "1. 시간 형식이 잘못되었습니다. [YYYY/MM/DD HH:MM]\n"
                            + "2. 중복 입력된 시간이 있습니다.\n"
                            + "3. 입력한 시간이 회의 가능 일시에 해당하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    SuccessResponse<UserTimeResponseDto> createMemberTime(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            final AvailableTimeRequestDto requestDto
    );
}
