package com.asap.server.presentation.controller.internal;

import com.asap.server.common.exception.Success;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.controller.internal.docs.MetricsControllerDocs;
import com.asap.server.service.internal.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal")
@RestController
@RequiredArgsConstructor
public class MetricsController implements MetricsControllerDocs {
    private final MetricsService metricsService;

    @GetMapping("/metrics")
    public SuccessResponse sendMetrics(
            @RequestParam("from") final String from,
            @RequestParam("to") final String to
    ) {
        metricsService.sendMetrics(from.trim(), to.trim());
        return SuccessResponse.success(Success.GET_METRICS_SUCCESS);
    }
}
