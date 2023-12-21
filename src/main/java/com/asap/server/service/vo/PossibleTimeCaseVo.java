package com.asap.server.service.vo;

import com.asap.server.domain.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class PossibleTimeCaseVo {
    private Duration duration;
    private int memberCnt;
}
