package com.asap.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableDate extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private MeetingV2 meeting;
    private LocalDate date;

    private AvailableDate(MeetingV2 meeting, LocalDate date) {
        this.meeting = meeting;
        this.date = date;
    }

    public static AvailableDate of(final MeetingV2 meeting,
                                   final LocalDate date) {
        return new AvailableDate(meeting, date);
    }
}
