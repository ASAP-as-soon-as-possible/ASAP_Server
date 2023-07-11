package com.asap.server.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.Place;
import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User host;
    @OneToMany
    private List<User> users;
    @OneToMany
    private List<DateAvailability> dateAvailabilities;
    @OneToMany
    private List<PreferTime> preferTimes;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Place place;
    private String placeDetail;
    @Column(nullable = false)
    private Duration duration;
    private String additionalInfo;
    @Column(nullable = false)
    private String url;
    private String imageUrl;
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
}
