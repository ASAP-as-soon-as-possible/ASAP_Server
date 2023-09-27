package com.asap.server.common.utils;

import com.asap.server.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {
    public static LocalDate transformLocalDate(final String month, final String day) {
        return LocalDate.of(2023, Integer.parseInt(month), Integer.parseInt(day));
    }

    public static String getMonth(final LocalDate localDate) {
        return String.valueOf(localDate.getMonthValue());
    }

    public static String getMonth(final LocalDateTime localDateTime) {
        return String.valueOf(localDateTime.getMonthValue());
    }

    public static String getDay(final LocalDate localDate) {
        return String.valueOf(localDate.getDayOfMonth());
    }

    public static String getDay(final LocalDateTime localDateTime) {
        return String.valueOf(localDateTime.getDayOfMonth());
    }

    public static String getDayOfWeek(final LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public static String getDayOfWeek(final LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public static String getTime(final LocalDateTime localDateTime) {
        return localDateTime.toLocalTime().toString();
    }

    public static LocalTime parseTime(final String timeSlot) {
        if (timeSlot.equals(TimeSlot.SLOT_24_00.getTime())) {
            return LocalTime.parse("00:00");
        } else {
            return LocalTime.parse(timeSlot);
        }
    }
}
