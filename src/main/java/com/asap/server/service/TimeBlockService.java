package com.asap.server.service;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import com.asap.server.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final TimeBlockRepository timeBlockRepository;

    public List<TimeBlock> getTimeBlocksByAvailableDate(final AvailableDate availableDate) {
        return timeBlockRepository.findByAvailableDate(availableDate);
    }
}
