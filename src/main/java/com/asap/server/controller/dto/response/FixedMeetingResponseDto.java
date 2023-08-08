package com.asap.server.controller.dto.response;

import com.asap.server.domain.ConfirmedDateTime;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
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

    public static FixedMeetingResponseDto of(Meeting meeting) {
        List<String> userNames = meeting
                .getFixedUsers()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());

        ConfirmedDateTime confirmedDateTime = meeting.getConfirmedDateTime();
        String month = String.valueOf(confirmedDateTime.getStartDateTime().getMonthValue());
        String day = String.valueOf(confirmedDateTime.getStartDateTime().getDayOfMonth());
        String dayOfWeek = confirmedDateTime.getStartDateTime().getDayOfWeek().toString();
        String startTime = confirmedDateTime.getStartDateTime().toLocalTime().toString();
        String endTime = confirmedDateTime.getEndDateTime().toLocalTime().toString();

        return FixedMeetingResponseDto
                .builder()
                .title(meeting.getTitle())
                .place(meeting.getPlace().toString())
                .placeDetail(meeting.getPlace().getPlaceDetail())
                .month(month)
                .day(day)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .hostName(meeting.getHost().getName())
                .userNames(userNames)
                .additionalInfo(meeting.getAdditionalInfo())
                .build();
    }
}
