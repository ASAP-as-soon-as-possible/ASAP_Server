package com.asap.server.infra.slack;

import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.WebhookPayloads;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MetricsSlackEventListener {
    @Value("${slack.webhook.metrics-url}")
    private String webhookUrl;

    @Async
    @EventListener
    public void sendMetrics(final MetricsEvent metricsEvent) throws IOException {
        List<LayoutBlock> layoutBlocks = generateLayoutBlock(metricsEvent.metrics());

        Slack.getInstance().send(webhookUrl, WebhookPayloads
                .payload(p -> p.blocks(layoutBlocks)));
    }

    private List<LayoutBlock> generateLayoutBlock(final Map<String, String> metrics) {
        return Blocks.asBlocks(
                getHeader("ASAP 데이터"),
                Blocks.divider(),
                getSection(generateMetricsMessage(metrics))
        );
    }

    private String generateMetricsMessage(Map<String, String> metrics) {
        StringBuilder sb = new StringBuilder();

        metrics.forEach((key, value) -> {
            sb.append(key).append(" : ").append(value).append("\n");
        });

        return sb.toString();
    }

    private LayoutBlock getHeader(final String text) {
        return Blocks.header(h -> h.text(
                plainText(pt -> pt.emoji(true)
                        .text(text))));
    }

    private LayoutBlock getSection(final String message) {
        return Blocks.section(s ->
                s.text(BlockCompositions.markdownText(message)));
    }
}
