package com.asap.server.common.utils;

import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.WebhookPayloads;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private StringBuilder sb= new StringBuilder();

    private String NEW_LINE = "\n";
    private String DOUBLE_NEW_LINE = "\n\n";

    //slack으로 알림보내기
    public void sendAlert(Exception error, HttpServletRequest request) throws IOException{
        // 메시지 내용인 LayoutBlock List 생성
        List layoutBlocks= generateLayoutBlock(error,request);

        // 슬랙의 send API과 webhookURL을 통해 생성한 메시지 내용 전송
        Slack.getInstance().send(webhookUrl, WebhookPayloads
                .payload(p->
                        // 메시지 전송 유저명
                        p.username("Exception is detected")
                                // 메시지 전송 유저 아이콘 이미지 URL
                                .iconUrl("<https://ibb.co/wzqtz8G>")
                                // 메시지 내용
                                .blocks(layoutBlocks)));
    }

    // 전체 메시지가 담긴 LayoutBlock 생성
    private List generateLayoutBlock(Exception error, HttpServletRequest request) {
        return Blocks.asBlocks(
                getHeader("서버 측 오류로 예상되는 예외 상황이 발생하였습니다."),
                Blocks.divider(),
                getSection(generateErrorMessage(error)),
                Blocks.divider(),
                getSection(generateErrorPointMessage(request)),
                Blocks.divider(),
                // 이슈 생성을 위해 프로젝트의 Issue URL을 입력하여 바로가기 링크를 생성
                getSection("[이슈 생성] (https://github.com/ASAP-as-soon-as-posiible/ASAP_Server/issues)")
        );
    }


    // 예외 정보 메시지 생성
    private String generateErrorMessage(Exception error) {
        sb.setLength(0);
        sb.append("*[Exception]*" + NEW_LINE + error.toString() + DOUBLE_NEW_LINE);
        sb.append("*[From]*" + NEW_LINE + readRootStackTrace(error) + DOUBLE_NEW_LINE);

        return sb.toString();
    }

    // HttpServletRequest를 사용하여 예외발생 요청에 대한 정보 메시지 생성
    private String generateErrorPointMessage(HttpServletRequest request) {
        sb.setLength(0);
        sb.append("*[Details]*" + NEW_LINE);
        sb.append("Request URL : " + request.getRequestURL().toString() + NEW_LINE);
        sb.append("Request Method : " + request.getMethod() + NEW_LINE);
        sb.append("Request Time : " + new Date() + NEW_LINE);

        return sb.toString();
    }

    // 예외발생 클래스 정보 return
    private String readRootStackTrace(Exception error) {
        return error.getStackTrace()[0].toString();
    }
    // 에러 로그 메시지의 제목 return
    private LayoutBlock getHeader(String text) {
        return Blocks.header(h->h.text(
                plainText(pt->pt.emoji(true)
                        .text(text))));
    }

    // 에러 로그 메시지 내용 return
    private LayoutBlock getSection(String message) {
        return Blocks.section(s->
                s.text(BlockCompositions.markdownText(message)));
    }

}
