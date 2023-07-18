package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PossibleTimeCaseDto {
    Duration duration;
    int memberCnt;
}
