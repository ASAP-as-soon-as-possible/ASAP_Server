package com.asap.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingTime {
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
    private String startTime;
    @Column(nullable = false)
    private String endTime;
}
