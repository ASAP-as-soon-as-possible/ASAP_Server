package com.asap.server.domain;

import com.asap.server.domain.enums.Duration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
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
    private ConfirmedDateTime confirmedDateTime;

    public boolean authenticateHost(final String name, final String password) {
        return this.host.getName().equals(name) && this.password.equals(password);
    }

    public boolean isConfirmedMeeting() {
        return this.confirmedDateTime != null;
    }


    public void setHost(final UserV2 user) {
        this.host = user;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
