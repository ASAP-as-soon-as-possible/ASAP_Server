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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Embedded
    @Column(nullable = false)
    private PasswordInfo passwordInfo;

    @Column(nullable = false)
    private String additionalInfo;

    private String url;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Duration duration;

    @OneToOne(fetch = FetchType.LAZY)
    private User host;

    @Embedded
    private Place place;

    @Embedded
    private ConfirmedDateTime confirmedDateTime;

    public boolean authenticateHost(final String name, final String password) {
        return this.host.getName().equals(name) && this.passwordInfo.getPassword().equals(password);
    }

    public boolean authenticateHost(final Long userId) {
        return this.host.getId().equals(userId);
    }

    public boolean isConfirmedMeeting() {
        return this.confirmedDateTime != null;
    }

    public void setConfirmedDateTime(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        this.confirmedDateTime = new ConfirmedDateTime(startDateTime, endDateTime);
    }

    public void setHost(final User user) {
        this.host = user;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
