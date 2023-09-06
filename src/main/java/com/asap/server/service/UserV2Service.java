package com.asap.server.service;

import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.Role;
import com.asap.server.repository.UserV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserV2Service {
    private final UserV2Repository userV2Repository;

    public UserV2 createHost(MeetingV2 meeting, String hostName) {
        UserV2 user = UserV2.of(meeting, hostName, Role.HOST);
        userV2Repository.save(user);
        return user;
    }
}
