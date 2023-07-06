package com.asap.server.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private String place;
    private String placeDetail;
    @Column(nullable = false)
    private String duration;
    private String additionalInfo;
    @Column(nullable = false)
    private String url;
    private String imageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
