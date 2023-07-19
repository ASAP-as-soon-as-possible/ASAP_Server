package com.asap.server.controller.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TimeSlotInfoDto {
    private final List<UserDto> users = new ArrayList<>();
    private int weight = 0;

    public void addUserName(UserDto user) {
        users.add(user);
    }

    public void addWeight(int weight) {
        this.weight += weight;
    }
}
