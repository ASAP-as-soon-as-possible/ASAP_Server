package com.asap.server.service.vo;

import com.asap.server.persistence.domain.enums.Duration;

public record PossibleTimeCaseVo(Duration duration, int memberCnt) {
}
