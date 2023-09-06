package com.asap.server.domain;

import com.asap.server.domain.enums.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingV2 extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String additionalInfo;

    private String url;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Duration duration;

    @OneToOne
    private UserV2 host;

    @Embedded
    private Place place;

    @Embedded
    private ConfirmedDateTime ConfirmedDateTime;

    public boolean authenticateHost(final String name, final String password) {
        return this.host.getName().equals(name) && this.password.equals(password);
    }


    private MeetingV2(String title,
                      String password,
                      String additionalInfo,
                      Duration duration,
                      Place place) {
        this.title = title;
        this.password = password;
        this.additionalInfo = additionalInfo;
        this.duration = duration;
        this.place = place;
    }

    public static MeetingV2 of(final String title,
                               final String password,
                               final String additionalInfo,
                               final Duration duration,
                               final Place place) {
        return new MeetingV2(title,
                password,
                additionalInfo,
                duration,
                place);
    }

    public void setHost(final UserV2 user) {
        this.host = user;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
