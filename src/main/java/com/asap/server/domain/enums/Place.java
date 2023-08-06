package com.asap.server.domain.enums;

import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum Place {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    UNDEFINED("UNDEFINED");

    @Getter
    @JsonValue
    private String place;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Place findByTime(String place) {
        return Stream.of(Place.values())
                .filter(c -> c.getPlace().equals(place))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Error.INVALID_JSON_INPUT_EXCEPTION));
    }
}
