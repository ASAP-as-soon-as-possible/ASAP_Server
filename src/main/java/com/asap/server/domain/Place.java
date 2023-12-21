package com.asap.server.domain;

import com.asap.server.domain.enums.PlaceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Place {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;
    private String placeDetail;
}
