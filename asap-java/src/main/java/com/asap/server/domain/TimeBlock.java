package com.asap.server.domain;

import com.asap.server.domain.enums.TimeSlot;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeBlock extends AuditingTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault(value = "0")
    private int weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "available_date_id")
    private AvailableDate availableDate;

    @Enumerated(value = EnumType.STRING)
    private TimeSlot timeSlot;

    @OneToMany(mappedBy = "timeBlock")
    private List<TimeBlockUser> timeBlockUsers;

    public void addTimeBlockUsers(TimeBlockUser timeBlockUser) {
        if (timeBlockUsers == null) {
            timeBlockUsers = new ArrayList<>();
        }
        timeBlockUsers.add(timeBlockUser);
    }

    public void addWeight(int weight) {
        this.weight += weight;
    }
}
