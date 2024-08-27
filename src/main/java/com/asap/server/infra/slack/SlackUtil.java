package com.asap.server.infra.slack;

import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookPayloads;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final StringBuilder sb= new StringBuilder();

    private final String NEW_LINE = "\n";
    private final String DOUBLE_NEW_LINE = "\n\n";

    public void sendAlert(Exception error, HttpServletRequest request) throws IOException{
        List<LayoutBlock> layoutBlocks= generateLayoutBlock(error,request);

        Slack.getInstance().send(webhookUrl, WebhookPayloads
                .payload(p -> p.blocks(layoutBlocks)));
    }

    private List<LayoutBlock> generateLayoutBlock(Exception error, HttpServletRequest request) {
        return Blocks.asBlocks(
                getHeader("Internal Server Error Detected"),
                Blocks.divider(),
                getSection(generateErrorMessage(error)),
                Blocks.divider(),
                getSection(generateErrorPointMessage(request)),
                Blocks.divider(),
                getSection("<https://github.com/ASAP-as-soon-as-posiible/ASAP_Server/issues | Go To Make Issue >")
        );
    }


    private String generateErrorMessage(Exception error) {
        sb.setLength(0);
        sb.append("*[Exception]*").append(NEW_LINE).append(error.toString()).append(DOUBLE_NEW_LINE);
        sb.append("*[From]*").append(NEW_LINE).append(readRootStackTrace(error)).append(DOUBLE_NEW_LINE);

        return sb.toString();
    }

    private String generateErrorPointMessage(HttpServletRequest request) {
        sb.setLength(0);
        sb.append("*[Details]*").append(NEW_LINE);
        sb.append("Request URL : ").append(request.getRequestURL().toString()).append(NEW_LINE);
        sb.append("Request Method : ").append(request.getMethod()).append(NEW_LINE);
        sb.append("Request Time : ").append(new Date()).append(NEW_LINE);

        return sb.toString();
    }

    private String readRootStackTrace(Exception error) {
        return error.getStackTrace()[0].toString();
    }

    private LayoutBlock getHeader(String text) {
        return Blocks.header(h->h.text(
                plainText(pt->pt.emoji(true)
                        .text(text))));
    }

    private LayoutBlock getSection(String message) {
        return Blocks.section(s->
                s.text(BlockCompositions.markdownText(message)));
    }
}
