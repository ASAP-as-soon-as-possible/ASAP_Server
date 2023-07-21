package com.asap.server.service.vo;

import com.asap.server.domain.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class PossibleTimeCaseVo {
    Duration duration;
    int memberCnt;
}
