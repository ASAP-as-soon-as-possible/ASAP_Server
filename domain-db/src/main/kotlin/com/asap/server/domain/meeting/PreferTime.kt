package com.asap.server.domain.meeting

import com.asap.server.domain.common.AuditingTimeEntity
import com.asap.server.domain.common.TimeSlot
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne


@Entity
class PreferTime(
    id: Long? = null,
    meeting: Meeting,
    startTime: TimeSlot,
    endTime: TimeSlot,
) : AuditingTimeEntity() {
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