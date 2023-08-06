package com.asap.server.domain;

import com.asap.server.domain.enums.PlaceType;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Place {
    private PlaceType placeType;

    @Column(nullable = true)
    private String placeDetail;

    public Place(PlaceType placeType, String placeDetail) {
        this.placeType = placeType;
        this.placeDetail = placeDetail;
    }
}
