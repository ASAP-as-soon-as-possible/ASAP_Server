package com.asap.server.common.utils;

import com.asap.server.exception.model.BadRequestException;
import java.time.DayOfWeek;

public class DayOfWeekConverter {
    public static String convertDayOfWeekEnToKo(DayOfWeek dayOfWeekEn) {
        String dayOfWeekKr;
        switch (dayOfWeekEn) {
            case MONDAY:
                dayOfWeekKr = "월";
                break;
            case TUESDAY:
                dayOfWeekKr = "화";
                break;
            case WEDNESDAY:
                dayOfWeekKr = "수";
                break;
            case THURSDAY:
                dayOfWeekKr = "목";
                break;
            case FRIDAY:
                dayOfWeekKr = "금";
                break;
            case SATURDAY:
                dayOfWeekKr = "토";
                break;
            case SUNDAY:
                dayOfWeekKr = "일";
                break;
            default: throw new BadRequestException(String.format("%s 값은 유효하지 않은 입력 값입니다.", dayOfWeekEn));
        }
        return dayOfWeekKr;
    }
}
