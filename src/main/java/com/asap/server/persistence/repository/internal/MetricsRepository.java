package com.asap.server.persistence.repository.internal;

import static com.asap.server.persistence.domain.QMeeting.meeting;
import static com.asap.server.persistence.domain.user.QUser.user;

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
                .where(meeting.createdAt.between(from, to))
                .fetchOne();
    }

    public Long countTotalConfirmedMeetingCount(final LocalDateTime from, final LocalDateTime to) {
        return jpaQueryFactory
                .select(meeting.count())
                .from(meeting)
                .where(
                        meeting.confirmedDateTime.confirmedStartTime.isNotNull()
                                .and(meeting.createdAt.between(from, to))
                )
                .fetchOne();
    }

    public Long countTotalUserCount(final LocalDateTime from, final LocalDateTime to) {
        return jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(user.createdAt.between(from, to))
                .fetchOne();
    }
}
