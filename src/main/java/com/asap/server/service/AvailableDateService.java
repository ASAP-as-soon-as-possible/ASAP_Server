package com.asap.server.service;

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
}
