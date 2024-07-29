package com.asap.server.persistence.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmedDateTime {
    private LocalDateTime confirmedStartTime;
    private LocalDateTime confirmedEndTime;
}
