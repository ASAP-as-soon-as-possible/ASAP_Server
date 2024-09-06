package com.asap.server.persistence.repository.user;

import com.asap.server.persistence.domain.Meeting;
import java.util.List;

public interface UserRepositoryCustom {
    void updateUserIsFixedByMeeting(final Meeting meeting, final List<Long> users);
}
