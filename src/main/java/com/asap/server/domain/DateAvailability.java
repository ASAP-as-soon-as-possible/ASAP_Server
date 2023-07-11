package com.asap.server.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateAvailability {
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
        String dayOfWeek = date.substring(11,14);
        return new DateAvailability(month, day, dayOfWeek);
    }

}
