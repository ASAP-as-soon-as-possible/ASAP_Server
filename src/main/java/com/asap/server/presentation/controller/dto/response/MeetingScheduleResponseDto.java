package com.asap.server.presentation.controller.dto.response;

import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
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
}
