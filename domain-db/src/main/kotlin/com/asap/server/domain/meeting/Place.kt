package com.asap.server.domain.meeting

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class Place(
        @Column(nullable = false, name = "place_type")
        @Enumerated(value = EnumType.STRING)
        val type: PlaceType,
        @Column(nullable = false, name = "place_detail")
        val detail: String
)
