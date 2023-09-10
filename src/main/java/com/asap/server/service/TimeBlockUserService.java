package com.asap.server.service;

import com.asap.server.controller.dto.response.TimeSlotDto;
import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.UserV2;
import com.asap.server.repository.TimeBlockUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeBlockUserService {
    private final TimeBlockUserRepository timeBlockUserRepository;

    @Transactional
    public TimeBlockUser create(final TimeBlock timeBlock, final UserV2 userV2) {
        TimeBlockUser timeBlockUser = TimeBlockUser.builder()
                .user(userV2)
                .timeBlock(timeBlock)
                .build();
        timeBlockUserRepository.save(timeBlockUser);
        return timeBlockUser;
    }

    public List<UserV2> findUsersByTimeBlock(final TimeBlock timeBlock) {
        return timeBlockUserRepository.findByTimeBlock(timeBlock)
                .stream()
                .map(TimeBlockUser::getUser)
                .collect(Collectors.toList());
    }

    public TimeSlotDto getTimeSlotDto(final TimeBlock timeBlock, final int memberCount) {
        TimeSlotDto timeSlotDto = TimeSlotDto.builder()
                        .time(timeBlock.getTimeSlot().getTime())
                        .userNames(findUsersByTimeBlock(timeBlock).stream().map(UserV2::getName).collect(Collectors.toList()))
                .build();
        timeSlotDto.setColorLevel(memberCount);
        return timeSlotDto;
    }
}