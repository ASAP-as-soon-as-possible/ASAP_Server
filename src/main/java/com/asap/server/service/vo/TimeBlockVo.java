package com.asap.server.service.vo;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class TimeBlockVo {
    private Long id;
    private int weight;
    private AvailableDateVo availableDate;
    private TimeSlot timeSlot;
    private List<UserVo> users;

    public static TimeBlockVo of(TimeBlock timeBlock) {
        return new TimeBlockVo(
                timeBlock.getId(),
                timeBlock.getWeight(),
                AvailableDateVo.of(timeBlock.getAvailableDate()),
                timeBlock.getTimeSlot(),
                timeBlock.getTimeBlockUsers()
                        .stream()
                        .map(TimeBlockUser::getUser)
                        .map(UserVo::of)
                        .collect(Collectors.toList())
        );
    }
}
