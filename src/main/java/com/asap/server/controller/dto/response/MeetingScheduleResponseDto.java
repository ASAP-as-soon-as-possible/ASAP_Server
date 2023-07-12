package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Duration;
import com.asap.server.domain.enums.Place;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;


@Getter
@AllArgsConstructor
public class MeetingScheduleResponseDto {

    private Duration duration;
    private Place place;
    private String placeDetail;
    private List<AvailableDateResponseDto> availableDates;
    private List<PreferTimeResponseDto> preferTimes;
}
