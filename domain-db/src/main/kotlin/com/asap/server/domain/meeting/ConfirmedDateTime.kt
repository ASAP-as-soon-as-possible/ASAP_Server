package com.asap.server.domain.meeting

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class ConfirmedDateTime(
    @Column(name = "confirmed_start_time")
    val startTime: LocalDateTime,
    @Column(name = "confirmed_end_time")
    val endTime: LocalDateTime
)