package com.asap.server.domain.enums;

import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Duration {
    HALF("HALF", 2),
    HOUR("HOUR", 3),
    HOUR_HALF("HOUR_HALF", 4),
    TWO_HOUR("TWO_HOUR", 5),
    TWO_HOUR_HALF("TWO_HOUR_HALF", 6),
    THREE_HOUR("THREE_HOUR", 7);


    @JsonValue
    private String duration;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Duration findByTime(String duration) {
        return Stream.of(Duration.values())
                .filter(c -> c.getDuration().equals(duration))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Error.INVALID_JSON_INPUT_EXCEPTION));
    }
    private final String duration;
    private final int needBlock;
}