package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMeetingTimeSaveRequestDto {

    @NotBlank
    @Schema(description = "희의 가능 시간 식별자", example = "1")
    private String id;

    @NotBlank
    @Schema(description = "회의 가능 시간 (월)", example = "1")
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String month;

    @NotBlank
    @Schema(description = "회의 가능 시간 (일)", example = "1")
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String day;

    @NotBlank
    @Schema(description = "회의 가능 시간 (요일)", example = "월")
    @Pattern(regexp = "[\\uAC00-\\uD7A3]", message = "날짜 형식이 맞지 않습니다.")
    private String dayOfWeek;

    @NotNull
    @Schema(description = "회의 가능 시간 (시작 시간)", example = "09:00")
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot startTime;

    @NotNull
    @Schema(description = "회의 가능 시간 (끝 시간)", example = "11:00")
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot endTime;

    @NotNull
    @Schema(description = "회의 가능 시간 우선 순위", example = "1")
    private int priority;
}