package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
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
    private String id;

    @NotBlank
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String month;

    @NotBlank
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String day;

    @NotBlank
    @Pattern(regexp = "[\\uAC00-\\uD7A3]", message = "날짜 형식이 맞지 않습니다.")
    private String dayOfWeek;

    @NotNull
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot startTime;

    @NotNull
    @Pattern(regexp = "\\d\\d:\\d\\d", message = "시간은 hh:mm 형식이어야 합니다.")
    private TimeSlot endTime;

    @NotNull
    private int priority;
}