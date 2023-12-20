package com.asap.server.repository.user;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.QUserVo;
import com.asap.server.service.vo.UserVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.asap.server.domain.QTimeBlock.timeBlock;
import static com.asap.server.domain.QTimeBlockUser.timeBlockUser;
import static com.asap.server.domain.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void updateUserIsFixedByMeeting(Meeting meeting, List<Long> users) {
        queryFactory.update(user)
                .set(user.isFixed, true)
                .where(
                        user.meeting.eq(meeting)
                                .and(user.id.in(users))
                )
                .execute();
    }

    @Override
    public List<UserVo> findByAvailableDateAndTimeSlots(Long availableDateId, List<TimeSlot> timeSlots) {
        return queryFactory.select(new QUserVo(
                        user.id,
                        user.name)
                )
                .from(timeBlockUser)
                .innerJoin(timeBlock).on(timeBlockUser.timeBlock.id.eq(timeBlock.id)
                        .and(timeBlock.availableDate.id.eq(availableDateId))
                        .and(timeBlock.timeSlot.in(timeSlots)))
                .innerJoin(user).on(timeBlockUser.user.id.eq(user.id))
                .groupBy(user.id)
                .fetch();
    }
}
