package com.asap.server.controller.dto.response;

import com.asap.server.domain.enums.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingDto {
    List<DateAvailabilityDto> availabilities;
    Duration duration;
}
