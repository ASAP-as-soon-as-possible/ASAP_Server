package com.asap.server.presentation.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 가능 시간 DTO")
public class AvailableTimeRequestDto {
    @NotBlank
    @Size(max = 8 , message = "이름의 최대 입력 길이(8자)를 초과했습니다.")
    @Schema(description = "사용자 이름",example = "김아삽")
    private String name;

    @Schema(description = "가능 일자")
    private List<@Valid UserMeetingTimeSaveRequestDto> availableTimes;
}
