package com.asap.server.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateAvailability extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String month;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String dayOfWeek;

    private DateAvailability(String month,
                            String day,
                            String dayOfWeek){
        this.month = Integer.valueOf(month).toString();
        this.day = Integer.valueOf(day).toString();
        this.dayOfWeek = dayOfWeek;
    }

    public static DateAvailability newInstance(String date){
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String dayOfWeek = dayConverter(date.substring(11,14));

        return new DateAvailability(month, day, dayOfWeek);
    }
    public static String dayConverter(String s){
        switch (s) {
            case "MON":
                return "월";
            case "TUE":
                return "화";
            case "WED":
                return "수";
            case "THU":
                return "목";
            case "FRI":
                return "금";
            case "SAT":
                return "토";
            case "SUN":
                return "일";
            default:
                throw new BadRequestException(Error.VALIDATION_REQUEST_MISSING_EXCEPTION);
        }
    }
}
