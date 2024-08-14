package com.asap.server.presentation.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 가능 시간 DTO")
public class AvailableTimeRequestDto {
    @Schema(description = "사용자 이름",example = "김아삽")
    private String name;

    @Schema(description = "가능 일자")
    private List<@Valid UserMeetingTimeSaveRequestDto> availableTimes;
}
