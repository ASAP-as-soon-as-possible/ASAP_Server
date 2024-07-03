package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class MeetingScheduleResponseDto {
    private Duration duration;
    private PlaceType place;
    private String placeDetail;
    private List<AvailableDateResponseDto> availableDates;
    private List<PreferTimeResponseDto> preferTimes;
}
