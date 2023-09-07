package com.asap.server.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
public class ConfirmedDateTime {
    private LocalDateTime confirmedStartTime;
    private LocalDateTime confirmedEndTime;
}
