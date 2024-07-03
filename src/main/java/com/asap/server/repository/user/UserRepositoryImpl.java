package com.asap.server.repository.user;

import static com.asap.server.domain.QAvailableDate.availableDate;
import static com.asap.server.domain.QTimeBlock.timeBlock;
import static com.asap.server.domain.QTimeBlockUser.timeBlockUser;
import static com.asap.server.domain.QUser.user;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.service.vo.QUserVo;
import com.asap.server.service.vo.UserVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
    public List<UserVo> findByAvailableDateAndTimeSlots(LocalDate date, List<TimeSlot> timeSlots) {
        return queryFactory.select(
                        new QUserVo(
                                user.id,
                                user.name
                        )
                ).from(timeBlockUser)
                .innerJoin(timeBlock).on(timeBlockUser.timeBlock.id.eq(timeBlock.id))
                .innerJoin(user).on(timeBlockUser.user.id.eq(user.id))
                .innerJoin(availableDate).on(timeBlock.availableDate.id.eq(availableDate.id))
                .where(availableDate.date.eq(date).and(timeBlock.timeSlot.in(timeSlots)))
                .groupBy(user.id)
                .fetch();
    }
}
