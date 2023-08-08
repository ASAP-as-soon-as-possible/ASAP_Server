package com.asap.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private AvailableDate(LocalDate date) {
        this.date = date;
    }

    public static AvailableDate of(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return new AvailableDate(LocalDate.parse(date.substring(0, 10), formatter)));
    }
}
