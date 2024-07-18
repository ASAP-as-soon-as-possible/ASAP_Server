package com.asap.server.persistence.domain.enums;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Duration {
    HALF("HALF", 1),
    HOUR("HOUR", 2),
    HOUR_HALF("HOUR_HALF", 3),
    TWO_HOUR("TWO_HOUR", 4),
    TWO_HOUR_HALF("TWO_HOUR_HALF", 5),
    THREE_HOUR("THREE_HOUR", 6);


    @JsonValue
    private String duration;
    private final int needBlock;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Duration findByTime(String duration) {
        return Stream.of(Duration.values())
                .filter(c -> c.getDuration().equals(duration))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Error.INVALID_JSON_INPUT_EXCEPTION));
    }
}