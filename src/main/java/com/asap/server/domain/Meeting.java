package com.asap.server.domain;

import com.asap.server.domain.enums.Duration;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.asap.server.exception.Error;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.UnauthorizedException;
import lombok.AccessLevel;
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
    private List<PreferTime> preferTimes;
    @OneToMany
    private List<AvailableDate> availableDates;
    @OneToMany
    private List<User> fixedUsers;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Duration duration;
    private String additionalInfo;
    private String url;
    @Embedded
    private Place place;
    @Embedded
    private ConfirmedDateTime confirmedDateTime;

    private Meeting(User host,
                    List<AvailableDate> availableDates,
                    List<PreferTime> preferTimes,
                    List<User> users,
                    String password,
                    String title,
                    Place place,
                    Duration duration,
                    String additionalInfo
    ) {
        this.host = host;
        this.availableDates = availableDates;
        this.preferTimes = preferTimes;
        this.users = users;
        this.password = password;
        this.title = title;
        this.place = place;
        this.duration = duration;
        this.additionalInfo = additionalInfo;
    }

    public static Meeting newInstance(User host,
                                      List<AvailableDate> availableDates,
                                      List<PreferTime> preferTimes,
                                      List<User> users,
                                      String password,
                                      String title,
                                      Place place,
                                      Duration duration,
                                      String additionalInfo) {
        return new Meeting(host, availableDates, preferTimes, users, password, title, place, duration, additionalInfo);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFinalUsers(List<User> users) {
        this.fixedUsers = users;
    }

    public void setConfirmedDateTime(ConfirmedDateTime confirmedDateTime) {
        this.confirmedDateTime = confirmedDateTime;
    }

    public void authenticateHost(Long userId){
        if (!this.host.getId().equals(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
    }

    public void isFixedMeeting(){
        if (this.confirmedDateTime != null) {
            throw new ConflictException(Error.MEETING_VALIDATION_FAILED_EXCEPTION);
        }

    }
}
