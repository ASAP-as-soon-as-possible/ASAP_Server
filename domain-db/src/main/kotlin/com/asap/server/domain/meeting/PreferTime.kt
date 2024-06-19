package com.asap.server.domain.meeting

import jakarta.persistence.*

@Entity
class PreferTime(
        id: Long? = null,
        meeting: Meeting,
        startTime: TimeSlot,
        endTime: TimeSlot,
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
    @Enumerated(value = EnumType.STRING)
    var startTime: TimeSlot = startTime
        protected set

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var endTime: TimeSlot = endTime
        protected set
}