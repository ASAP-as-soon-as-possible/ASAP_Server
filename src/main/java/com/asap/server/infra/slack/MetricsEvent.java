package com.asap.server.infra.slack;

import java.util.Map;

public record MetricsEvent(Map<String, String> metrics) {
}
