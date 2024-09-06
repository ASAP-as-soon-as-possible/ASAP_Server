package com.asap.server.presentation.controller.user.docs;

import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.controller.dto.request.HostLoginRequestDto;
import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자", description = "사용자 및 로그인 관련 API 입니다.")
public interface UserRegisterControllerDocs {
    @Operation(summary = "[방장 입장 뷰] 방장 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공입니다"),
            @ApiResponse(responseCode = "400",
                    description = "1. 방장 이름의 최대 입력 길이(8자)를 초과했습니다.\n"
                            + "2. 비밀번호는 4자리 이상 숫자입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 사용자 이름 또는 비밀번호입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "회의 가능 시간이 입력되지 않았습니다."),
            @ApiResponse(responseCode = "404", description = "해당 회의는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 확정된 회의입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    SuccessResponse<HostLoginResponseDto> loginByHost(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            final HostLoginRequestDto requestDto
    );
}
