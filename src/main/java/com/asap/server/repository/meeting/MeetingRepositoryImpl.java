package com.asap.server.repository.meeting;

import com.asap.server.domain.Meeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.asap.server.domain.QMeeting.meeting;
import static com.asap.server.domain.QUser.user;

@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Meeting> findByIdWithHost(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(meeting)
                        .where(meeting.id.eq(id))
                        .join(meeting.host, user)
                        .fetchJoin()
                        .fetchOne()
        );
    }
}
