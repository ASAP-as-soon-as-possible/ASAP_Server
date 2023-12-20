package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회의 가능 시간 DTO")
public class UserMeetingTimeSaveRequestDto {

    @NotBlank
    @Schema(description = "희의 가능 시간 식별자", example = "1")
    private String id;

    @NotBlank
    @Schema(description = "회의 가능 시간 (월)", example = "01")
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String month;

    @NotBlank
    @Schema(description = "회의 가능 시간 (일)", example = "01")
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String day;

    @NotBlank
    @Schema(description = "회의 가능 시간 (요일)", example = "월")
    @Pattern(regexp = "[\\uAC00-\\uD7A3]", message = "날짜 형식이 맞지 않습니다.")
    private String dayOfWeek;

    @Schema(description = "회의 가능 시간 (시작 시간)", example = "09:00")
    private TimeSlot startTime;

    @Schema(description = "회의 가능 시간 (끝 시간)", example = "11:00")
    private TimeSlot endTime;

    @NotNull
    @Schema(description = "회의 가능 시간 우선 순위", example = "1")
    private int priority;
}