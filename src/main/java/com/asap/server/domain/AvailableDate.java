package com.asap.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableDate extends AuditingTimeEntity{
    @Id
    private Long id;
    private LocalDate date;
    @OneToMany
    private List<TimeBlock> timeBlocks;

    public AvailableDate(LocalDate date) {
        this.date = date;
    }
}
