package com.asap.server.persistence.domain.enums;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum PlaceType {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    UNDEFINED("UNDEFINED");

    @Getter
    @JsonValue
    private String place;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PlaceType findByTime(String place) {
        return Stream.of(PlaceType.values())
                .filter(c -> c.getPlace().equals(place))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Error.INVALID_JSON_INPUT_EXCEPTION));
    }
}
