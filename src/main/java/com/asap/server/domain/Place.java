package com.asap.server.domain;

import com.asap.server.domain.enums.PlaceType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Place {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;
    private String placeDetail;
}
