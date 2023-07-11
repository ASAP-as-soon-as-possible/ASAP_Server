package com.asap.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TimeSlot startTime;
    @Column(nullable = false)
    private TimeSlot endTime;

    private PreferTime(TimeSlot startTime, TimeSlot endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static PreferTime newInstance(TimeSlot startTime, TimeSlot endTime){
        return new PreferTime(startTime, endTime);
    }
}
