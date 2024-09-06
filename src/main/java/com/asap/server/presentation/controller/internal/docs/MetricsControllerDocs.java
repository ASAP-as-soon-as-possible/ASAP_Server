package com.asap.server.presentation.controller.internal.docs;

import com.asap.server.presentation.common.dto.ErrorResponse;
import com.asap.server.presentation.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ASAP 내부 API", description = "ASAP 내부에서 사용하는 API입니다.")
public interface MetricsControllerDocs {
    @Operation(summary = "[메트릭 조회] ASAP에 등록된 메트릭 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메트릭 정보 조회 성공입니다."),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 날짜를 입력했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    SuccessResponse sendMetrics(
            @Parameter(example = "2024-08-12") final String from,
            @Parameter(example = "2024-08-15") final String to
    );
}
