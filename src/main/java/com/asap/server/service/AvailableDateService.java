package com.asap.server.service;

import com.asap.server.common.utils.DayOfWeekConverter;
import com.asap.server.controller.dto.response.ScheduleDateResponseDto;
import com.asap.server.controller.dto.response.AvailableDateResponseDto;
import com.asap.server.domain.AvailableDate;
import com.asap.server.repository.AvailableDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailableDateService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final AvailableDateRepository availableDateRepository;
    private final TimeBlockService timeBlockService;
    public List<AvailableDate> create(List<String> availableDateStrings) {
        List<AvailableDate> availableDates = parseAndSortAvailableDates(availableDateStrings);
        availableDateRepository.saveAll(availableDates);
        return availableDates;
    }

    private List<AvailableDate> parseAndSortAvailableDates(List<String> availableDateStrings) {
        return availableDateStrings.stream()
                .map(dateString -> new AvailableDate(LocalDate.parse(dateString.substring(0, 10), formatter)))
                .sorted(Comparator.comparing(AvailableDate::getDate))
                .collect(Collectors.toList());
    }

    public List<ScheduleDateResponseDto> getAvailableDate(List<AvailableDate> availableDates) {
        return availableDates
                .stream()
                .map(dateAvailability -> new ScheduleDateResponseDto(
                        String.valueOf(dateAvailability.getDate().getMonthValue()),
                        String.valueOf(dateAvailability.getDate().getDayOfMonth()),
                        DayOfWeekConverter.convertDayOfWeekEnToKo(dateAvailability.getDate().getDayOfWeek())
                ))
                .collect(Collectors.toList());
    }

    public List<AvailableDateResponseDto> getTimeTableDates(List<AvailableDate> availableDates){
        return availableDates.stream()
                .map(availableDate -> AvailableDateResponseDto.of(
                        availableDate.getDate(),
                        timeBlockService.getTimeSlot(availableDate.getTimeBlocks())
                        ))
                .collect(Collectors.toList());
    }
}
