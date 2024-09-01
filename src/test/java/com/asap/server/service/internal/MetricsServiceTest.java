package com.asap.server.service.internal;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.infra.slack.MetricsEvent;
import com.asap.server.persistence.repository.internal.MetricsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class MetricsServiceTest {
    @Mock
    private MetricsRepository metricsRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private MetricsService metricsService;

    @DisplayName("날짜 형식은 yyyy-MM-dd 형식으로 입력한다.")
    @Test
    void test() {
        // given
        String fromStr = "2024-08-24";
        String toStr = "2024-08-26";

        LocalDateTime from = LocalDate.parse(fromStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        LocalDateTime to = LocalDate.parse(toStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        when(metricsRepository.countTotalMeetingCount(from, to)).thenReturn(1L);
        when(metricsRepository.countTotalUserCount(from, to)).thenReturn(1L);
        when(metricsRepository.countTotalConfirmedMeetingCount(from, to)).thenReturn(1L);
        Map<String, String> metrics = Map.of(
                "개설된 총 회의 수", "1",
                "사용한 총 사용자 수", "1",
                "확정된 총 회의 수", "1"
        );

        // when
        metricsService.sendMetrics(fromStr, toStr);

        // then
        verify(publisher, times(1)).publishEvent(new MetricsEvent(metrics));
    }

    @DisplayName("날짜 형식(yyyy-MM-dd)과 다른 형식으로 입력했을 때, BadRequestException을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"2024/08/24", "2024 08 24", "2024-13-24", "2024/08/32", "2024-13-30"})
    void test2(String fromStr) {
        // given
        String toStr = "2024-08-26";

        // when
        assertThatThrownBy(() -> metricsService.sendMetrics(fromStr, toStr))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("유효하지 않은 날짜를 입력했습니다.");
    }

    @DisplayName("시작, 종료 날짜 모두 들어오지 않을 수 있다.")
    @Test
    void test3() {
        // given
        String fromStr = null;
        String toStr = null;

        when(metricsRepository.countTotalMeetingCount(null, null)).thenReturn(1L);
        when(metricsRepository.countTotalUserCount(null, null)).thenReturn(1L);
        when(metricsRepository.countTotalConfirmedMeetingCount(null, null)).thenReturn(1L);
        Map<String, String> metrics = Map.of(
                "개설된 총 회의 수", "1",
                "사용한 총 사용자 수", "1",
                "확정된 총 회의 수", "1"
        );

        // when
        metricsService.sendMetrics(fromStr, toStr);

        // then
        verify(publisher, times(1)).publishEvent(new MetricsEvent(metrics));
    }

    @DisplayName("시작 날짜는 들어오지 않고, 종료 날짜만 들어올 수 있다.")
    @Test
    void test4() {
        // given
        String fromStr = null;
        String toStr = "2024-08-26";

        LocalDateTime to = LocalDate.parse(toStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        when(metricsRepository.countTotalMeetingCount(null, to)).thenReturn(1L);
        when(metricsRepository.countTotalUserCount(null, to)).thenReturn(1L);
        when(metricsRepository.countTotalConfirmedMeetingCount(null, to)).thenReturn(1L);
        Map<String, String> metrics = Map.of(
                "개설된 총 회의 수", "1",
                "사용한 총 사용자 수", "1",
                "확정된 총 회의 수", "1"
        );

        // when
        metricsService.sendMetrics(fromStr, toStr);

        // then
        verify(publisher, times(1)).publishEvent(new MetricsEvent(metrics));
    }

    @DisplayName("시작 날짜는 들어오고, 종료 날짜는 들어오지 않을 수 있다.")
    @Test
    void test5() {
        // given
        String fromStr = "2024-08-26";
        String toStr = null;

        LocalDateTime from = LocalDate.parse(fromStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        when(metricsRepository.countTotalMeetingCount(from, null)).thenReturn(1L);
        when(metricsRepository.countTotalUserCount(from, null)).thenReturn(1L);
        when(metricsRepository.countTotalConfirmedMeetingCount(from, null)).thenReturn(1L);
        Map<String, String> metrics = Map.of(
                "개설된 총 회의 수", "1",
                "사용한 총 사용자 수", "1",
                "확정된 총 회의 수", "1"
        );

        // when
        metricsService.sendMetrics(fromStr, toStr);

        // then
        verify(publisher, times(1)).publishEvent(new MetricsEvent(metrics));
    }
}