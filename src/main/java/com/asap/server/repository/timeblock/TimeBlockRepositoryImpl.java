package com.asap.server.repository.timeblock;

import com.asap.server.service.vo.QTimeBlockVo;
import com.asap.server.service.vo.TimeBlockVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.asap.server.domain.QTimeBlock.timeBlock;
import static com.asap.server.domain.QTimeBlockUser.timeBlockUser;
import static com.asap.server.domain.QUser.user;

@RequiredArgsConstructor
public class TimeBlockRepositoryImpl implements TimeBlockRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TimeBlockVo> findByAvailableDate(Long availableId) {
        return queryFactory.select(new QTimeBlockVo(
                        timeBlock.weight.min().as("weight"),
                        timeBlock.timeSlot.as("timeSlot"),
                        user.id.count().as("userCount")
                ))
                .from(timeBlockUser)
                .innerJoin(timeBlock).on(
                        timeBlockUser.timeBlock.id.eq(timeBlock.id)
                                .and(timeBlock.availableDate.id.eq(availableId))
                )
                .innerJoin(user).on(timeBlockUser.user.id.eq(user.id))
                .groupBy(timeBlock.timeSlot)
                .fetch();
    }
}
