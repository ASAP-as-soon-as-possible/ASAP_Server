package com.asap.server.domain.timeblock

import com.asap.server.domain.common.AuditingTimeEntity
import com.asap.server.domain.common.TimeSlot
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.ColumnDefault

@Entity
class TimeBlock(
    id: Long? = null,
    weight: Int = 0,
    availableDateId: Long,
    timeSlot: TimeSlot
) : AuditingTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        protected set

    @ColumnDefault(value = "0")
    var weight = weight
        protected set

    @Column(nullable = false, name = "available_date_id")
    var availableDateId = availableDateId
        protected set

    @Enumerated(value = EnumType.STRING)
    var timeSlot = timeSlot
        protected set

    fun addWeight(weight: Int) {
        this.weight += weight
    }
}