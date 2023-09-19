package com.asap.server.service.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class TimeSlotInfoDto {
    private final List<UserVo> users = new ArrayList<>();
    private int weight = 0;

    public void addUserName(UserVo user) {
        users.add(user);
    }

    public void addWeight(int weight) {
        this.weight += weight;
    }
}
