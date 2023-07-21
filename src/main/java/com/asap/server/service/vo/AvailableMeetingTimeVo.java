package com.asap.server.service.vo;

import com.asap.server.domain.enums.TimeSlot;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AvailableMeetingTimeVo implements Comparable<AvailableMeetingTimeVo> {
    private String date;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private int weight;
    private List<UserVo> users;
    private boolean isFixed;

    @Override
    public int compareTo(AvailableMeetingTimeVo o) {
        if (this.users.size() == o.users.size()) {
            return Integer.compare(o.weight, this.weight);
        }
        return Integer.compare(o.users.size(), this.users.size());
    }

    public void setIsFixed() {
        this.isFixed = !this.isFixed;
    }
}
