package com.asap.server.domain.meeting

import com.asap.server.domain.common.AuditingTimeEntity
import jakarta.persistence.*

@Table(name = "meeting")
@Entity
class Meeting(
    id: Long? = null,
    title: String,
    password: String,
    additionalInfo: String,
    duration: Duration,
    place: Place,
    confirmedDateTime: ConfirmedDateTime
) : AuditingTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        protected set

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var password: String = password
        protected set

    @Column(nullable = false, name = "addtional_info")
    var additionalInfo: String = additionalInfo
        protected set

    @Column(nullable = false)
    var duration: Duration = duration
        protected set

    @Embedded
    var place: Place = place
        protected set

    @Embedded
    var confirmedDateTime: ConfirmedDateTime = confirmedDateTime
        protected set
}