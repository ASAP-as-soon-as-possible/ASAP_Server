package com.asap.server.domain;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.Place;
import com.asap.server.domain.enums.TimeSlot;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends AuditingTimeEntity {
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
    private String url;
    private String month;
    private String day;
    private String dayOfWeek;
    private TimeSlot startTime;
    private TimeSlot endTime;

    private Meeting(User host,
                    List<DateAvailability> dateAvailabilities,
                    List<PreferTime> preferTimes,
                    String password,
                    String title,
                    Place place,
                    String placeDetail,
                    Duration duration,
                    String additionalInfo
    ) {
        this.host = host;
        this.dateAvailabilities = dateAvailabilities;
        this.preferTimes = preferTimes;
        this.password = password;
        this.title = title;
        this.place = place;
        this.placeDetail = placeDetail;
        this.duration = duration;
        this.additionalInfo = additionalInfo;
    }

    public static Meeting newInstance(User host,
                                      List<DateAvailability> dateAvailabilities,
                                      List<PreferTime> preferTimes,
                                      String password,
                                      String title,
                                      Place place,
                                      String placeDetail,
                                      Duration duration,
                                      String additionalInfo) {
        return new Meeting(host, dateAvailabilities, preferTimes, password, title, place, placeDetail, duration, additionalInfo);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(TimeSlot startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(TimeSlot endTime) {
        this.endTime = endTime;
    }
}
