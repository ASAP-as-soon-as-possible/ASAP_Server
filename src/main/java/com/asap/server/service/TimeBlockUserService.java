package com.asap.server.service;

import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.UserV2;
import com.asap.server.repository.TimeBlockUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}