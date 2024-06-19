package com.asap.server.domain.meeting

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class AvailableDate(
        id: Long? = null,
        meeting: Meeting,
        date: LocalDate,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    var meeting: Meeting = meeting
        protected set

    @Column(nullable = false)
    var date: LocalDate = date
        protected set
}