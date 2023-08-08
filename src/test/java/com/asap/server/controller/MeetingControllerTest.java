package com.asap.server.controller;

import com.asap.server.controller.MeetingController;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.request.PreferTimeSaveRequestDto;
import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.PlaceType;
import com.asap.server.domain.enums.TimeSlot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MeetingControllerTest {

    @InjectMocks
    private MeetingController meetingController;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(meetingController).build();
    }

    @Test
    @Transactional
    @DisplayName("회의가 저장된다.")
    void createMeetingTest() throws Exception {
        //given
        List<PreferTimeSaveRequestDto> preferTimeDtos = new ArrayList<>();
        preferTimeDtos.add(new PreferTimeSaveRequestDto(TimeSlot.SLOT_6_00, TimeSlot.SLOT_12_00));
        MeetingSaveRequestDto saveRequestDto = new MeetingSaveRequestDto(
                "테스트 회의",
                Arrays.asList("2023/08/07/MON", "2023/08/08/TUE"),
                preferTimeDtos,
                PlaceType.OFFLINE,
                "어디서 만날까",
                Duration.HOUR_HALF,
                "서지원",
                "2028",
                "추가 정보 비밀"
        );
        //when, then
        mockMvc.perform(
                post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto))
        ).andExpect(status().isOk());

    }
}
