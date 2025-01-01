package com.asap.server.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {
    public static LocalDate transformLocalDate(final String month, final String day) {
        return LocalDate.of(2025, Integer.parseInt(month), Integer.parseInt(day));
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
        return parseTime(localDateTime.toLocalTime().toString());
    }

    public static LocalTime parseLocalTime(final String timeSlot) {
        return LocalTime.parse(parseTime(timeSlot));
    }

    private static String parseTime(final String timeSlot) {
        if (timeSlot.equals("24:00")) {
            return "00:00";
        } else if (timeSlot.equals("00:00")) {
            return "24:00";
        } else {
            return timeSlot;
        }
    }
}
