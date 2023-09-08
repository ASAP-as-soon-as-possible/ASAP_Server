package com.asap.server.domain;

import com.asap.server.domain.enums.TimeSlot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @ManyToOne
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
}
