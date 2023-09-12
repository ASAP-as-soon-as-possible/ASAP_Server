package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.controller.dto.request.UserRequestDto;
import com.asap.server.controller.dto.response.HostLoginResponseDto;
import com.asap.server.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.UserTimeResponseDto;
import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.Role;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ForbiddenException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import com.asap.server.repository.MeetingV2Repository;
import com.asap.server.repository.TimeBlockUserRepository;
import com.asap.server.repository.UserV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.asap.server.exception.Error.INVALID_MEETING_HOST_EXCEPTION;
import static com.asap.server.exception.Error.USER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserV2Service {
    private final TimeBlockUserRepository timeBlockUserRepository;
    private final UserV2Repository userV2Repository;
    private final TimeBlockService timeBlockService;
    private final TimeBlockUserService timeBlockUserService;
    private final AvailableDateService availableDateService;
    private final MeetingV2Repository meetingV2Repository;
    private final JwtService jwtService;

    public UserV2 createUser(final MeetingV2 meeting,
                             final String hostName,
                             final Role role) {
        UserV2 user = UserV2.builder()
                .meeting(meeting)
                .name(hostName)
                .role(role)
                .isFixed(false)
                .build();
        userV2Repository.save(user);
        return user;
    }

    @Transactional
    public UserMeetingTimeResponseDto createHostTime(final Long userId,
                                                     final String url,
                                                     final Long meetingId,
                                                     final List<UserMeetingTimeSaveRequestDto> requestDtos) {
        MeetingV2 meetingV2 = meetingV2Repository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meetingV2.authenticateHost(userId))
            throw new BadRequestException(INVALID_MEETING_HOST_EXCEPTION);

        isDuplicatedDate(requestDtos);
        requestDtos.forEach(requestDto -> createUserTimeBlock(meetingV2, meetingV2.getHost(), requestDto));

        String accessToken = jwtService.issuedToken(meetingV2.getHost().getId().toString());

        return UserMeetingTimeResponseDto.builder()
                .url(url)
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public UserTimeResponseDto createUserTime(final Long meetingId,
                                              final AvailableTimeRequestDto requestDto) {
        MeetingV2 meetingV2 = meetingV2Repository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        UserV2 userV2 = createUser(meetingV2, requestDto.getName(), Role.MEMBER);

        requestDto.getAvailableTimes().forEach(availableTime -> createUserTimeBlock(meetingV2, userV2, availableTime));

        return UserTimeResponseDto.builder()
                .role(Role.MEMBER.getRole())
                .build();
    }

    public List<String> findUserNameByMeeting(final MeetingV2 meetingV2) {
        List<UserV2> users = userV2Repository.findByMeeting(meetingV2);
        if (users.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_EXCEPTION);
        }
        return users.stream()
                .map(UserV2::getName)
                .collect(Collectors.toList());
    }

    private void createUserTimeBlock(final MeetingV2 meetingV2,
                                     final UserV2 userV2,
                                     final UserMeetingTimeSaveRequestDto requestDto) {
        AvailableDate availableDate = availableDateService.findByMeetingAndDate(meetingV2, requestDto.getMonth(), requestDto.getDay());
        TimeSlot.getTimeSlots(requestDto.getStartTime().ordinal(), requestDto.getEndTime().ordinal() - 1)
                .stream()
                .map(timeSlot -> timeBlockService.searchTimeBlock(timeSlot, availableDate, requestDto.getPriority())).collect(Collectors.toList())
                .forEach(timeBlock -> timeBlock.addTimeBlockUsers(timeBlockUserService.create(timeBlock, userV2)));
    }

    private void isDuplicatedDate(final List<UserMeetingTimeSaveRequestDto> requestDtoList) {
        Map<String, List<TimeSlot>> meetingTimeAvailable = new HashMap<>();
        for (UserMeetingTimeSaveRequestDto requestDto : requestDtoList) {
            String col = String.format("%s %s %s", requestDto.getMonth(), requestDto.getDay(), requestDto.getDayOfWeek());
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(requestDto.getStartTime().ordinal(), requestDto.getEndTime().ordinal() - 1);
            if (meetingTimeAvailable.containsKey(col)) {
                if (meetingTimeAvailable.get(col).stream().anyMatch(timeSlots::contains)) {
                    throw new BadRequestException(Error.DUPLICATED_TIME_EXCEPTION);
                }
            } else {
                meetingTimeAvailable.put(col, timeSlots);
            }
        }
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

    public int getMeetingUserCount(MeetingV2 meeting) {
        return userV2Repository.countByMeeting(meeting);
    }

    @Transactional
    public HostLoginResponseDto loginByHost(
            final Long meetingId,
            final HostLoginRequestDto requestDto
    ) {
        MeetingV2 meeting = meetingV2Repository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(requestDto.getName(), requestDto.getPassword()))
            throw new UnauthorizedException(Error.INVALID_HOST_ID_PASSWORD_EXCEPTION);

        if (isEmptyHostTimeBlock(meeting.getHost()))
            throw new ForbiddenException(Error.HOST_MEETING_TIME_NOT_PROVIDED);

        return HostLoginResponseDto
                .builder()
                .accessToken(jwtService.issuedToken(meeting.getHost().getId().toString()))
                .build();
    }

    private Boolean isEmptyHostTimeBlock(UserV2 host) {
        List<TimeBlockUser> hostTimeBlocks = timeBlockUserRepository.findAllByUser(host);
        return hostTimeBlocks.isEmpty();
    }
}
