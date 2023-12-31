package com.asap.server.controller.dto.request;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.PlaceType;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회의 생성 DTO")
public class MeetingSaveRequestDto {

    @NotBlank(message = "회의 제목이 입력되지 않았습니다.")
    @Size(max = 15, message = "제목의 최대 입력 길이(15자)를 초과했습니다.")
    @Schema(description = "회의 주제")
    private String title;

    @Schema(description = "회의 가능 날짜", example = "[\"2023/07/09/MON\"]")
    private List<@Pattern(regexp = "\\d\\d\\d\\d/\\d\\d/\\d\\d/[a-zA-Z][a-zA-Z][a-zA-Z]", message = "회의 가능 날짜 형식은 YYYY/mm/dd/ddd 입니다.") String> availableDates;

    @Schema(description = "회의 선호 시간")
    private List<PreferTimeSaveRequestDto> preferTimes;

    @NotNull(message = "회의 형식이 입력되지 않았습니다.")
    @Schema(description = "회의 방식", example = "ONLINE", allowableValues = {"ONLINE", "OFFLINE", "UNDEFINED"})
    private PlaceType place;

    @Schema(description = "회의 장소 설명")
    private String placeDetail;

    @Schema(description = "회의 진행 시간", example = "HALF", allowableValues = {"HALF" , "HOUR", "HOUR_HALF", "TWO_HOUR", "TWO_HOUR_HALF", "THREE_HOUR"})
    @NotNull(message = "회의 진행 시간이 입력되지 않았습니다.")
    private Duration duration;

    @Schema(description = "회의 방장 이름", example = "김아삽")
    @NotBlank(message = "방장의 이름이 입력되지 않았습니다.")
    @Size(max = 8, message = "방장 이름의 최대 입력 길이(8자)를 초과했습니다.")
    private String name;

    @Schema(description = "회의 비밀번호", example = "0808")
    @NotBlank(message = "회의 비밀번호가 입력되지 않았습니다.")
    @Size(min = 4, message = "비밀번호의 최소 입력 길이는 4자입니다.")
    @Pattern(regexp = "\\d{4,}", message = "비밀번호는 4자리 이상 숫자입니다.")
    private String password;

    @Schema(description = "회의 추가 정보")
    @Size(max = 50, message = "추가 내용의 최대 입력 길이(50자)를 초과했습니다.")
    private String additionalInfo;
}
