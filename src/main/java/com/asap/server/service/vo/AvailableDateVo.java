package com.asap.server.service.vo;


import com.asap.server.domain.AvailableDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AvailableDateVo {
    private Long id;
    private LocalDate date;

    public static AvailableDateVo of(AvailableDate availableDate) {
        return new AvailableDateVo(availableDate.getId(), availableDate.getDate());
    }
}
