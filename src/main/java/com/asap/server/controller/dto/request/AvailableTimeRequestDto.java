package com.asap.server.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class AvailableTimeRequestDto {
    @NotBlank
    @Size(max = 8 , message = "방장 이름의 최대 입력 길이(8자)를 초과했습니다.")
    private String name;

    private List<UserMeetingTimeSaveRequestDto> availableTimes;
}
