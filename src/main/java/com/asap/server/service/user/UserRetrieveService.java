package com.asap.server.service.user;

import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.repository.user.UserRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRetrieveService {
    private final UserRepository userRepository;

    public int getMeetingUserCount(final Meeting meeting) {
        return userRepository.countByMeeting(meeting);
    }

    public Map<Long, User> getUserIdToUserMap(final Long meetingId) {
        return userRepository
                .findAllByMeetingId(meetingId).stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }
}
