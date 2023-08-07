package com.asap.server.domain;

import com.asap.server.domain.enums.PlaceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {
    private PlaceType placeType;
    @Column(nullable = true)
    private String placeDetail;
}
