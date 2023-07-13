package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.TimeSlot;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingConfirmRequestDto {
    @NotNull(message = "회의 진행 월이 입력되지 않았습니다.")
    private String month;
    @NotNull(message = "회의 진행 날짜가 입력되지 않았습니다.")
    private String day;
    @NotNull(message = "회의 진행 요일이 입력되지 않았습니다.")
    private String dayOfWeek;
    @NotNull(message = "회의 시작 시간이 입력되지 않았습니다.")
    private TimeSlot startTime;
    @NotNull(message = "회의 종료 시간이 입력되지 않았습니다.")
    private TimeSlot endTime;
}