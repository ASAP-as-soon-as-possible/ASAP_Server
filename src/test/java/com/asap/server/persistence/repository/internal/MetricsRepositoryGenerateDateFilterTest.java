package com.asap.server.persistence.repository.internal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MetricsRepositoryGenerateDateFilterTest {
    private static final String INSERT_QUERY_TEMPLATE = "INSERT INTO meeting (title, password, duration, place_type, additional_info, created_at) VALUES ('title', '1234', 'HALF','ONLINE', '', ?)";

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private EntityManager em;

    @DisplayName(
            """
                1. 2022년 5월 20일 데이터
                2. 2023년 1월 1일 데이터
                3. 2023년 6월 15일 데이터
                4. 2023년 12월 31일 데이터
            """
    )
    @BeforeEach
    public void setUp() {
        em.createNativeQuery(INSERT_QUERY_TEMPLATE)
                .setParameter(1, "2022-05-20T10:00:00")
                .executeUpdate();

        em.createNativeQuery(INSERT_QUERY_TEMPLATE)
                .setParameter(1, "2023-01-01T12:00:00")
                .executeUpdate();

        em.createNativeQuery(INSERT_QUERY_TEMPLATE)
                .setParameter(1, "2023-06-15T15:30:00")
                .executeUpdate();

        em.createNativeQuery(INSERT_QUERY_TEMPLATE)
                .setParameter(1, "2023-12-31T23:59:59")
                .executeUpdate();
    }

    @DisplayName("시작 날짜와 종료 날짜 모두가 주어졌을 때, 주어진 범위 내의 결과를 반환한다.")
    @Test
    public void test() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 12, 31, 0, 0);

        // when
        Long count = metricsRepository.countTotalMeetingCount(from, to);

        // then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("시작 날짜가 주어지고 종료 날짜가 주어지지 않았을 때, 시작 날짜 이후의 결과를 반환한다.")
    @Test
    public void test2() {
        // given
        LocalDateTime from = LocalDateTime.of(2023, 6, 1, 0, 0);
        LocalDateTime to = null;

        // when
        Long count = metricsRepository.countTotalMeetingCount(from, to);

        // then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("종료 날짜가 주어지고 시작 날짜가 주어지지 않았을 때, 종료 날짜 이전의 결과를 반환한다.")
    @Test
    public void test3() {
        // given
        LocalDateTime from = null;
        LocalDateTime to = LocalDateTime.of(2023, 1, 1, 0, 0);

        // when
        Long count = metricsRepository.countTotalMeetingCount(from, to);

        // then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("날짜 범위가 주어지지 않았을 때, 전범위를 반환한다.")
    @Test
    public void test4() {
        // given
        LocalDateTime from = null;
        LocalDateTime to = null;

        // when
        Long count = metricsRepository.countTotalMeetingCount(from, to);

        // then
        assertThat(count).isEqualTo(4);
    }
}
