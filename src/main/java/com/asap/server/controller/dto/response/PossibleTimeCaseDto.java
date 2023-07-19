package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class PossibleTimeCaseDto {
    Duration duration;
    int memberCnt;
}
