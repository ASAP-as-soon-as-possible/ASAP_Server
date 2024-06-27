package com.asap.server.domain.user

import com.asap.server.domain.common.AuditingTimeEntity
import jakarta.persistence.*

@Entity
class User(
    id: Long? = null,
    meetingId: Long,
    name: String,
    isFixed: Boolean,
    role: Role
) : AuditingTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        protected set

    @Column(nullable = false, name = "meeting_id")
    var meetingId: Long = meetingId
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var isFixed: Boolean = isFixed
        protected set

    @Column(nullable = false)
    var role: Role = role
        protected set
}