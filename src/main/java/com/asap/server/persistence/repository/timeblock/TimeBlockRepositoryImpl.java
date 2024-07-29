package com.asap.server.persistence.repository.timeblock;

import static com.asap.server.persistence.domain.QAvailableDate.availableDate;
import static com.asap.server.persistence.domain.QTimeBlock.timeBlock;
import static com.asap.server.persistence.domain.QTimeBlockUser.timeBlockUser;

import com.asap.server.persistence.repository.timeblock.dto.QTimeBlockDto;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
