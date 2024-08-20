package com.asap.server.presentation.controller.dto.request;

import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.service.dto.UserMeetingScheduleRegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "회의 가능 시간 DTO")
public record UserMeetingTimeSaveRequestDto(
        @NotBlank
        @Schema(description = "회의 가능 시간 (월)", example = "7")
        String month,

        @NotBlank
        @Schema(description = "회의 가능 시간 (일)", example = "9")
        String day,

        @Schema(description = "회의 가능 시간 (시작 시간)", example = "09:00")
        TimeSlot startTime,

        @Schema(description = "회의 가능 시간 (끝 시간)", example = "11:00")
        TimeSlot endTime,

        @NotNull
        @Schema(description = "회의 가능 시간 우선 순위", example = "1")
        int priority
) {
        public static List<UserMeetingScheduleRegisterDto> to(final List<UserMeetingTimeSaveRequestDto> targets) {
                return targets.stream().map(
                        t -> new UserMeetingScheduleRegisterDto(
                                t.month(),
                                t.day(),
                                t.startTime(),
                                t.endTime(),
                                t.priority()
                        )).toList();
        }
}