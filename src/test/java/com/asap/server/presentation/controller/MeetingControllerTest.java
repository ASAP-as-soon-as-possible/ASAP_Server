package com.asap.server.presentation.controller;

import com.asap.server.presentation.controller.MeetingController;
import com.asap.server.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

@ExtendWith(MockitoExtension.class)
public class MeetingControllerTest {

    @InjectMocks
    private MeetingController target;

    @Mock
    private MeetingService meetingService;


    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    @DisplayName("정상적인 요청이 들어왔을 때")
    public void saveCorrectRequest() throws Exception {
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
        //given
        String request = "{\"title\" : \"ASAP 회의\", \"availableDates\" : [\"2023/09/25/MON\", \"2023/09/18/MON\", \"2023/09/15/FRI\" ],\"preferTimes\" : [{ \"startTime\": \"18:00\", \"endTime\": \"24:00\"}],\"place\" : \"UNDEFINED\",\"placeDetail\" : \"\",\"duration\" : \"HOUR\", \"name\" : \"김아삽\", \"password\" : \"1111\" ,\"additionalInfo\" : \"\"}";

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/meeting")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
