package com.asap.server.domain;

import com.asap.server.domain.enums.PlaceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Place {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PlaceType placeType;
    private String placeDetail;

    public static Place of(PlaceType placeType, String placeDetail) {
        return new Place(placeType, placeDetail);
    }
}
