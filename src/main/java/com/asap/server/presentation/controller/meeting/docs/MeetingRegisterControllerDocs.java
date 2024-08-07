package com.asap.server.presentation.controller.meeting.docs;

import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.presentation.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.presentation.controller.dto.response.MeetingSaveResponseDto;
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
public interface MeetingRegisterControllerDocs {
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
    SuccessResponse<MeetingSaveResponseDto> create(final MeetingSaveRequestDto meetingSaveRequestDto);

    @Operation(summary = "[큐 카드] 큐 카드 확정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의 시간 확정 성공입니다."),
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
    @SecurityRequirement(name = "JWT Auth")
    SuccessResponse confirmMeeting(
            @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long meetingId,
            final MeetingConfirmRequestDto meetingConfirmRequestDto,
            @Parameter(hidden = true) final Long userId
    );
}
