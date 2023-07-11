package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.Place;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingSaveRequestDto {

    @NotBlank(message = "회의 제목이 입력되지 않았습니다.")
    @Size(max = 15 , message = "제목의 최대 입력 길이(15자)를 초과했습니다.")
    private String title;

    private List<String> availableDateList;

    private List<PreferTimeSaveRequestDto> preferTimeSaveRequestDtoList;

    @NotNull(message = "회의 형식이 입력되지 않았습니다.")
    private Place place;

    private String placeDetail;

    @NotNull(message = "회의 진행 시간이 입력되지 않았습니다.")
    private Duration duration;

    @NotBlank(message = "방장의 이름이 입력되지 않았습니다.")
    @Size(max = 8 , message = "방장 이름의 최대 입력 길이(8자)를 초과했습니다.")
    private String name;

    @NotBlank(message = "회의 비밀번호가 입력되지 않았습니다.")
    @Size(min = 4, message = "비밀번호의 최소 입력 길이는 4자입니다.")
    @Pattern(regexp = "\\d{4,}", message = "비밀번호는 4자리 이상 숫자입니다.")
    private String password;

    @Size(max = 50, message = "추가 내용의 최대 입력 길이(50자)를 초과했습니다.")
    private String additionalInfo;
}
