package com.asap.server.persistence.domain;

import com.asap.server.persistence.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferTime extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TimeSlot startTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TimeSlot endTime;

}
