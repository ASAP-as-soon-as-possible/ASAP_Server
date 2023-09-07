package com.asap.server.service;

import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.Role;
import com.asap.server.repository.UserV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserV2Service {
    private final UserV2Repository userV2Repository;

    public UserV2 createUser(final MeetingV2 meeting,
                             final String hostName,
                             final Role role) {
        UserV2 user = UserV2.builder().meeting(meeting)
                .name(hostName)
                .role(role)
                .isFixed(false)
                .build();
        userV2Repository.save(user);
        return user;
    }

    public List<String> getFixedUsers(final MeetingV2 meeting) {
        return userV2Repository
                .findByMeetingAndIsFixed(meeting, true)
                .stream()
                .map(UserV2::getName)
                .collect(Collectors.toList());
    }
}
