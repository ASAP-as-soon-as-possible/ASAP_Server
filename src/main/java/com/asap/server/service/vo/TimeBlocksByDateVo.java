package com.asap.server.service.vo;


import com.asap.server.domain.AvailableDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class TimeBlocksByDateVo {
    private Long id;
    private LocalDate date;
    private List<TimeBlockVo> timeBlocks;

    public static TimeBlocksByDateVo of(AvailableDate availableDate, List<TimeBlockVo> timeBlocks) {
        return new TimeBlocksByDateVo(availableDate.getId(), availableDate.getDate(), timeBlocks);
    }
}
