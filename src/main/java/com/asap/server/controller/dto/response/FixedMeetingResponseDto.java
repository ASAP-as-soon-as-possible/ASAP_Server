package com.asap.server.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class FixedMeetingResponseDto {
    private String title;
    private String place;
    private String placeDetail;
    private String month;
    private String day;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String hostName;
    private List<String> userNames;
    private String additionalInfo;
}
