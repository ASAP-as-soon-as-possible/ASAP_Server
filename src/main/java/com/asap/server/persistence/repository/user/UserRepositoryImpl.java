package com.asap.server.persistence.repository.user;

import static com.asap.server.persistence.domain.user.QUser.user;

import com.asap.server.persistence.domain.Meeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
