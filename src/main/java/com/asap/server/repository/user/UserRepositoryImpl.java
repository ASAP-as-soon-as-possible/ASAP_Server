package com.asap.server.repository.user;

import com.asap.server.domain.Meeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
