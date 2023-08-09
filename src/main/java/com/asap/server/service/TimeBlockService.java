package com.asap.server.service;

import com.asap.server.controller.dto.response.TimeBlockResponseDto;
import com.asap.server.domain.TimeBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeBlockService {
    private final UserService userService;

    public List<TimeBlockResponseDto> getTimeSlot(List<TimeBlock> timeBlocks) {
        return timeBlocks.stream()
                .map(timeBlock -> TimeBlockResponseDto.of(timeBlock.getTime().getTime(),
                                userService.getUserNames(timeBlock.getUsers()),
                                timeBlock.getUsers().size()))
                .collect(Collectors.toList());
    }
}
