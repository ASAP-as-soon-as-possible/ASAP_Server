package com.asap.server.controller.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateDto{

    @NotBlank
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String month;

    @NotBlank
    @Pattern(regexp = "\\d{2}", message = "날짜 형식이 맞지 않습니다.")
    private String day;

    @NotBlank
    @Pattern(regexp = "[\\uAC00-\\uD7A3]", message = "날짜 형식이 맞지 않습니다.")
    private String dayOfWeek;
}