package com.asap.server.presentation.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "방장 로그인 DTO")
public record HostLoginRequestDto(
        @NotBlank
        @Size(max = 8, message = "방장 이름의 최대 입력 길이(8자)를 초과했습니다.")
        @Schema(description = "방장 이름", example = "김아삽")
        String name,
        @NotBlank
        @Pattern(regexp = "\\d{4,}", message = "비밀번호는 4자리 이상 숫자입니다.")
        @Schema(description = "회의 비밀번호", example = "0808")
        String password
) {
}
