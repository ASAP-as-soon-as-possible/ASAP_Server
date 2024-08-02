package com.asap.server.persistence.domain.enums;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum TimeSlot {
    SLOT_6_00("06:00", 0),
    SLOT_6_30("06:30", 1),
    SLOT_7_00("07:00", 2),
    SLOT_7_30("07:30", 3),
    SLOT_8_00("08:00", 4),
    SLOT_8_30("08:30", 5),
    SLOT_9_00("09:00", 6),
    SLOT_9_30("09:30", 7),
    SLOT_10_00("10:00", 8),
    SLOT_10_30("10:30", 9),
    SLOT_11_00("11:00", 10),
    SLOT_11_30("11:30", 11),
    SLOT_12_00("12:00", 12),
    SLOT_12_30("12:30", 13),
    SLOT_13_00("13:00", 14),
    SLOT_13_30("13:30", 15),
    SLOT_14_00("14:00", 16),
    SLOT_14_30("14:30", 17),
    SLOT_15_00("15:00", 18),
    SLOT_15_30("15:30", 19),
    SLOT_16_00("16:00", 20),
    SLOT_16_30("16:30", 21),
    SLOT_17_00("17:00", 22),
    SLOT_17_30("17:30", 23),
    SLOT_18_00("18:00", 24),
    SLOT_18_30("18:30", 25),
    SLOT_19_00("19:00", 26),
    SLOT_19_30("19:30", 27),
    SLOT_20_00("20:00", 28),
    SLOT_20_30("20:30", 29),
    SLOT_21_00("21:00", 30),
    SLOT_21_30("21:30", 31),
    SLOT_22_00("22:00", 32),
    SLOT_22_30("22:30", 33),
    SLOT_23_00("23:00", 34),
    SLOT_23_30("23:30", 35),
    SLOT_24_00("24:00", 36);

    @Getter
    @JsonValue
    private final String time;
    private final int index;

    //유저 가능 Time Slot 을 start 부터 end 까지 추가
    public static List<TimeSlot> getTimeSlots(int start, int end) {
        TimeSlot[] timeSlots = TimeSlot.values();
        List<TimeSlot> result = new ArrayList<>();
        for (TimeSlot t : timeSlots) {
            if (t.ordinal() >= start && t.ordinal() <= end) {
                result.add(t);
            }
        }
        return result;
    }

    // 특정 ordinal 의 time slot 반환
    public static TimeSlot getTimeSlot(int ordinal) {
        TimeSlot[] timeSlots = TimeSlot.values();
        for (TimeSlot t : timeSlots) {
            if (t.ordinal() == ordinal) return t;
        }
        return null;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TimeSlot findByTime(String timeSlot) {
        return Stream.of(TimeSlot.values())
                .filter(c -> c.getTime().equals(timeSlot))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Error.INVALID_JSON_INPUT_EXCEPTION));
    }
}
