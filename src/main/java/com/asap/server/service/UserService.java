package com.asap.server.service;

import static com.asap.server.common.exception.Error.INVALID_MEETING_HOST_EXCEPTION;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.common.jwt.JwtService;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.persistence.repository.user.UserRepository;
import com.asap.server.presentation.controller.dto.request.UserRequestDto;
import com.asap.server.presentation.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.presentation.controller.dto.response.UserTimeResponseDto;
import com.asap.server.service.time.UserMeetingScheduleService;
import com.asap.server.service.time.dto.register.UserMeetingScheduleRegisterDto;
import com.asap.server.service.time.dto.register.UserTimeRegisterDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final JwtService jwtService;
    private final UserMeetingScheduleService userMeetingScheduleService;

    public User createUser(final Meeting meeting,
                           final Name userName,
                           final Role role) {
        User user = User.builder()
                .meeting(meeting)
                .name(userName)
                .role(role)
                .isFixed(false)
                .build();
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserMeetingTimeResponseDto createHostTime(
            final Long meetingId,
            final Long hostId,
            final List<UserMeetingScheduleRegisterDto> requestDtos
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(hostId)) {
            throw new UnauthorizedException(INVALID_MEETING_HOST_EXCEPTION);
        }

        if (!userMeetingScheduleService.isEmptyHostTimeBlock(meeting.getHost().getId())) {
            throw new ConflictException(Error.HOST_TIME_EXIST_EXCEPTION);
        }

        userMeetingScheduleService.createUserMeetingSchedule(meetingId, hostId, requestDtos);

        String accessToken = jwtService.issuedToken(meeting.getHost().getId().toString());

        return UserMeetingTimeResponseDto.builder()
                .url(meeting.getUrl())
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public UserTimeResponseDto createUserTime(
            final Long meetingId,
            final UserTimeRegisterDto registerDto
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        User user = createUser(meeting, new Name(registerDto.name()), Role.MEMBER);

        userMeetingScheduleService.createUserMeetingSchedule(meetingId, user.getId(), registerDto.availableSchedules());

        return UserTimeResponseDto.builder()
                .role(Role.MEMBER.getRole())
                .build();
    }


    public List<String> getFixedUsers(final Meeting meeting) {
        return userRepository
                .findByMeetingAndIsFixed(meeting, true)
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }

    public void setFixedUsers(final Meeting meeting, final List<UserRequestDto> users) {
        List<Long> userIds = users.stream()
                .mapToLong(UserRequestDto::getId)
                .boxed()
                .collect(Collectors.toList());
        userRepository.updateUserIsFixedByMeeting(meeting, userIds);
    }
}
