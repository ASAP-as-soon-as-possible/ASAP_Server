package com.asap.server.domain;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class ConfirmedDateTime {
    private LocalDateTime confirmedStartTime;
    private LocalDateTime confirmedEndTime;
}
