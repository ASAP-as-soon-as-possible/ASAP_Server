package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.controller.dto.request.UserRequestDto;
import com.asap.server.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.UserV2;
import com.asap.server.domain.enums.Role;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.MeetingV2Repository;
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
    private final UserV2Repository userV2Repository;
    private final TimeBlockService timeBlockService;
    private final TimeBlockUserService timeBlockUserService;
    private final AvailableDateService availableDateService;
    private final MeetingV2Repository meetingV2Repository;
    private final JwtService jwtService;

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

    @Transactional
    public UserMeetingTimeResponseDto createHostTime(final Long userId,
                                                     final String url,
                                                     final Long meetingId,
                                                     final List<UserMeetingTimeSaveRequestDto> requestDtos) {
        MeetingV2 meetingV2 = meetingV2Repository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId))
            throw new BadRequestException(INVALID_MEETING_HOST_EXCEPTION);

        isDuplicatedDate(requestDtos);
        requestDtos.forEach(requestDto -> createUserTimeBlock(meetingV2, meetingV2.getHost(), requestDto));

        String accessToken = jwtService.issuedToken(meetingV2.getHost().getId().toString());

        return UserMeetingTimeResponseDto.builder()
                .url(url)
                .accessToken(accessToken)
                .build();
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
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(requestDto.getStartTime().ordinal(), requestDto.getEndTime().ordinal());
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
}
