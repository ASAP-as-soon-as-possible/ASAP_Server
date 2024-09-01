package com.asap.server.persistence.repository.internal;

import static com.asap.server.persistence.domain.QMeeting.meeting;
import static com.asap.server.persistence.domain.user.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MetricsRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Long countTotalMeetingCount(final LocalDateTime from, final LocalDateTime to) {
        return jpaQueryFactory
                .select(meeting.count())
                .from(meeting)
                .where(generateDateFilter(meeting.createdAt, from, to))
                .fetchOne();
    }

    public Long countTotalConfirmedMeetingCount(final LocalDateTime from, final LocalDateTime to) {
        return jpaQueryFactory
                .select(meeting.count())
                .from(meeting)
                .where(
                        meeting.confirmedDateTime.confirmedStartTime.isNotNull()
                                .and(generateDateFilter(meeting.createdAt, from, to))
                )
                .fetchOne();
    }

    public Long countTotalUserCount(final LocalDateTime from, final LocalDateTime to) {
        return jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(generateDateFilter(user.createdAt, from, to))
                .fetchOne();
    }

    private BooleanExpression generateDateFilter(
            final DateTimePath<LocalDateTime> createdAt,
            final LocalDateTime from,
            final LocalDateTime to
    ) {
        if (from != null && to != null) {
            return createdAt.between(from, to);
        }
        if (from != null) {
            return createdAt.after(from);
        }
        if (to != null) {
            return createdAt.before(to);
        }
        return null;
    }
}
