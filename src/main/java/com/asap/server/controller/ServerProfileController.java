package com.asap.server.controller;

import com.asap.server.config.resolver.meeting.MeetingId;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Hidden
public class ServerProfileController {

    private final Environment env;

    @GetMapping("/profile/{meetingId}")
    public String getProfile(
            @PathVariable("meetingId") String meetingUrl,
            @MeetingId Long meetingId
    ) {
        System.out.println("id is " + meetingId.toString());
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
