package com.asap.server.service.time.dto.retrieve;


import java.util.List;

public record TimeTableRetrieveDto(
        int memberCount,
        List<String> totalUserNames,
        List<AvailableDatesRetrieveDto> availableDateTimes
) {
    public static TimeTableRetrieveDto of(final List<String> totalUserNames, final List<AvailableDatesRetrieveDto> availableDateTimes) {
        return new TimeTableRetrieveDto(totalUserNames.size(), totalUserNames, availableDateTimes);
    }
}
