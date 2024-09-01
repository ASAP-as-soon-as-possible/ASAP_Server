package com.asap.server.concurrency;

import com.asap.server.presentation.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DuplicatedInterceptorTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("같은 사용자에게 회의 생성 요청이 연속해서 들어온다면 429 에러를 반환한다")
    public void multipleRequestTest() throws Exception {
        // given
        int numberOfThread = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);

        MeetingSaveRequestDto bodyDto = new MeetingSaveRequestDto(
                "title",
                List.of("2024/07/09/MON", "2024/07/10/TUE"),
                PlaceType.OFFLINE,
                "회의 장소 설명",
                Duration.HOUR,
                "방장 이름",
                "1234",
                "회의 추가 정보"
        );

        String body = objectMapper.writeValueAsString(bodyDto);

        // when
        List<MvcResult> results = new ArrayList<>();
        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                try {
                    MvcResult result = mockMvc.perform(
                                    post("/meeting")
                                            .header("x-real-ip", "0.0.0.1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(body)
                            )
                            .andReturn();
                    synchronized (results) {
                        results.add(result);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        int count200 = 0;
        int count429 = 0;
        for (MvcResult mvcResult : results) {
            if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) count200++;
            else if (mvcResult.getResponse().getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) count429++;
        }

        assertThat(count200).isEqualTo(1);
        assertThat(count429).isEqualTo(numberOfThread - 1);
    }
}
