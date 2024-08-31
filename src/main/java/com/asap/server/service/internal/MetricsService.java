package com.asap.server.service.internal;

import static com.asap.server.common.exception.Error.INVALID_DATE_FORMAT_EXCEPTION;

import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.infra.slack.MetricsEvent;
import com.asap.server.persistence.repository.internal.MetricsRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsRepository metricsRepository;
    private final ApplicationEventPublisher publisher;

    public void sendMetrics(final String fromStr, final String toStr) {
        if (!isValidDate(fromStr) || !isValidDate(toStr)) {
            throw new BadRequestException(INVALID_DATE_FORMAT_EXCEPTION);
        }

        LocalDateTime from = LocalDate.parse(fromStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        LocalDateTime to = LocalDate.parse(toStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        Map<String, String> metrics = new HashMap<>();
        metrics.put("개설된 총 회의 수", String.valueOf(metricsRepository.countTotalMeetingCount(from, to)));
        metrics.put("사용한 총 사용자 수", String.valueOf(metricsRepository.countTotalUserCount(from, to)));
        metrics.put("확정된 총 회의 수", String.valueOf(metricsRepository.countTotalConfirmedMeetingCount(from, to)));

        publisher.publishEvent(new MetricsEvent(metrics));
    }

    private boolean isValidDate(final String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
