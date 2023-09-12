package com.asap.server.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
@Builder
public class TimeSlotDto {
    private String time;
    private List<String> userNames;
    private int colorLevel;

    public void setColorLevel(final int memberCount) {
        double ratio = (double) userNames.size() / memberCount;

        if (ratio <= 0.2) {
            this.colorLevel = 1;
        } else if (ratio <= 0.4) {
            this.colorLevel = 2;
        } else if (ratio <= 0.6) {
            this.colorLevel = 3;
        } else if (ratio <= 0.8) {
            this.colorLevel = 4;
        } else if (ratio <= 1.0) {
            this.colorLevel = 5;
        } else {
            this.colorLevel = 0;
        }
    }
}
