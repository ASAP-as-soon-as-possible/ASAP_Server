package com.asap.server.persistence.domain;

import com.asap.server.persistence.domain.enums.Duration;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends AuditingTimeEntity {
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

    @OneToOne(fetch = FetchType.LAZY)
    private User host;

    @Embedded
    private Place place;

    @Embedded
    private ConfirmedDateTime confirmedDateTime;


    public boolean authenticateHost(final Long userId) {
        return this.host.getId().equals(userId);
    }

    public boolean checkHostName(final String name) {
        return this.host.getName().equals(name);
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
