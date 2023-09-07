package com.asap.server.service;

import com.asap.server.controller.dto.request.UserRequestDto;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.Role;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.UserV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.asap.server.exception.Error.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserV2Service {
    private final UserV2Repository userV2Repository;

    public UserV2 createHost(final MeetingV2 meeting,
                             final String hostName) {
        UserV2 user = UserV2.builder().meeting(meeting)
                .name(hostName)
                .role(Role.HOST)
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

    public void setFixedUsers(final List<UserRequestDto> users) {
        users.forEach(user -> {
            UserV2 fixedUser = userV2Repository
                    .findById(user.getId())
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
            fixedUser.setIsFixed(true);
        });
    }
}
