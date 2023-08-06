package com.asap.server.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class ConfirmedTime {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ConfirmedTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
