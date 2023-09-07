package com.asap.server.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class DateUtil {
    public static String getMonth(LocalDate localDate) {
        return String.valueOf(localDate.getMonthValue());
    }

    public static String getDay(LocalDate localDate) {
        return String.valueOf(localDate.getDayOfMonth());
    }
    public static String getDayOfWeek(LocalDate localDate) {
        return String.valueOf(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
    }
}
