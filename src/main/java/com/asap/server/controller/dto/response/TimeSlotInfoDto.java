package com.asap.server.controller.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TimeSlotInfoDto {
    private final List<String> userNames = new ArrayList<>();
    private int weight = 0;

    public void addUserName(String name) {
        userNames.add(name);
    }

    public void addWeight(int weight) {
        this.weight += weight;
    }
}
