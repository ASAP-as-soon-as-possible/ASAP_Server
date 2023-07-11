package com.asap.server.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeSlot {
    SLOT_6_00("06:00"),
    SLOT_6_30("06:30"),
    SLOT_7_00("07:00"),
    SLOT_7_30("07:30"),
    SLOT_8_00("08:00"),
    SLOT_8_30("08:30"),
    SLOT_9_00("09:00"),
    SLOT_9_30("09:30"),
    SLOT_10_00("10:00"),
    SLOT_10_30("10:30"),
    SLOT_11_00("11:00"),
    SLOT_11_30("11:30"),
    SLOT_12_00("12:00"),
    SLOT_12_30("12:30"),
    SLOT_13_00("13:00"),
    SLOT_13_30("13:30"),
    SLOT_14_00("14:00"),
    SLOT_14_30("14:30"),
    SLOT_15_00("15:00"),
    SLOT_15_30("15:30"),
    SLOT_16_00("16:00"),
    SLOT_16_30("16:30"),
    SLOT_17_00("17:00"),
    SLOT_17_30("17:30"),
    SLOT_18_00("18:00"),
    SLOT_18_30("18:30"),
    SLOT_19_00("19:00"),
    SLOT_19_30("19:30"),
    SLOT_20_00("20:00"),
    SLOT_20_30("20:30"),
    SLOT_21_00("21:00"),
    SLOT_21_30("21:30"),
    SLOT_22_00("22:00"),
    SLOT_22_30("22:30"),
    SLOT_23_00("23:00"),
    SLOT_23_30("23:30"),
    SLOT_24_00("24:00");

    @Getter
    @JsonValue
    private final String time;
}
