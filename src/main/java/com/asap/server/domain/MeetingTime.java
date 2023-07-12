package com.asap.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingTime extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private int priority;
    @Column(nullable = false)
    private String month;
    @Column(nullable = false)
    private String day;
    @Column(nullable = false)
    private String dayOfWeek;
    @Column(nullable = false)
    private TimeSlot startTime;
    @Column(nullable = false)
    private TimeSlot endTime;

    private MeetingTime(User user,
                        int priority,
                        String month,
                        String day,
                        String dayOfWeek,
                        TimeSlot startTime,
                        TimeSlot endTime){
        this.user = user;
        this.priority = priority;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static MeetingTime newInstance(User user,
                                          int priority,
                                          String month,
                                          String day,
                                          String dayOfWeek,
                                          TimeSlot startTime,
                                          TimeSlot endTime){
        return new MeetingTime(user, priority, month, day, dayOfWeek, startTime, endTime);
    }
}
