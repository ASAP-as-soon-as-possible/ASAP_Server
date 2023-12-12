package com.asap.server.repository.user;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.User;

import java.util.List;

public interface UserRepositoryCustom {
    void updateUserIsFixedByMeeting(final Meeting meeting, final List<Long> users);
}
