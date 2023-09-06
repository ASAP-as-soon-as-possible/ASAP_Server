package com.asap.server.service;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import com.asap.server.repository.AvailableDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailableDateService {
    private final AvailableDateRepository availableDateRepository;

    private LocalDate dateFormatter(final String stringOfDate) {
        return LocalDate.parse(stringOfDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public void create(final MeetingV2 meeting, final List<String> availableDates) {
        availableDates
                .stream()
                .map(s -> availableDateRepository.save(
                        AvailableDate.builder()
                                .meeting(meeting)
                                .date(dateFormatter(s.substring(0, 10)))
                                .build()
                )).collect(Collectors.toList());
    }
}
