package com.asap.server.repository.timeblock;

import com.asap.server.repository.timeblock.dto.QTimeBlockDto;
import com.asap.server.repository.timeblock.dto.TimeBlockDto;
import com.asap.server.service.vo.QTimeBlockVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.asap.server.domain.QAvailableDate.availableDate;
import static com.asap.server.domain.QTimeBlock.timeBlock;
import static com.asap.server.domain.QTimeBlockUser.timeBlockUser;
import static com.asap.server.domain.QUser.user;

@RequiredArgsConstructor
public class TimeBlockRepositoryImpl implements TimeBlockRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TimeBlockDto> findAllTimeBlockByMeeting(Long meetingId) {
        return queryFactory.select(
                        new QTimeBlockDto(
                                availableDate.date.as("availableDate"),
                                timeBlock.timeSlot.as("timeSlot"),
                                timeBlock.weight.min().as("weight"),
                                timeBlockUser.user.id.count().as("userCount")
                        )
                ).from(availableDate)
                .innerJoin(timeBlock).on(timeBlock.availableDate.id.eq(availableDate.id))
                .innerJoin(timeBlockUser).on(timeBlockUser.timeBlock.id.eq(timeBlock.id))
                .where(availableDate.meeting.id.eq(meetingId))
                .groupBy(availableDate.date, timeBlock.timeSlot)
                .fetch();
    }
}
