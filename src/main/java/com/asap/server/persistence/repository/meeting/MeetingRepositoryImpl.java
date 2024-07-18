package com.asap.server.persistence.repository.meeting;

import static com.asap.server.persistence.domain.QMeeting.meeting;
import static com.asap.server.persistence.domain.QUser.user;

import com.asap.server.persistence.domain.Meeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

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
