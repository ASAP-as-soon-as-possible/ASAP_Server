package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.controller.dto.request.HostLoginRequestDto;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.controller.dto.response.HostLoginResponseDto;
import com.asap.server.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.UserTimeResponseDto;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.Role;
import com.asap.server.domain.enums.TimeSlot;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import com.asap.server.exception.model.ForbiddenException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.MeetingTimeRepository;
import com.asap.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingTimeRepository meetingTimeRepository;
    private final JwtService jwtService;

    @Transactional
    public User createHost(String name) {
        User newUser = User.newInstance(name, Role.HOST);
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional
    public HostLoginResponseDto loginByHost(
            Long meetingId,
            HostLoginRequestDto requestDto
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        if (requestDto.getName().equals(meeting.getHost().getName())
                && requestDto.getPassword().equals(meeting.getPassword())) {
            isHostMeetingTimeSet(meeting.getHost());
            return HostLoginResponseDto
                    .builder()
                    .accessToken(jwtService.issuedToken(meeting.getHost().getId().toString()))
                    .build();
        } else {
            throw new UnauthorizedException(Error.INVALID_HOST_ID_PASSWORD_EXCEPTION);
        }
    }

    public void isHostMeetingTimeSet(User host) {
        List<MeetingTime> meetingTimeList = meetingTimeRepository.findByUser(host);
        if (meetingTimeList.isEmpty()) {
            throw new ForbiddenException(Error.HOST_MEETING_TIME_NOT_PROVIDED);
        }
    }

    public UserTimeResponseDto createMemberMeetingTime(
            Long meetingId,
            AvailableTimeRequestDto requestDto
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        User newUser = User.newInstance(requestDto.getName(), Role.MEMBER);
        meeting.getUsers().add(newUser);
        userRepository.save(newUser);
        createMeetingTimeList(newUser, requestDto.getAvailableTimes());
        return UserTimeResponseDto
                .builder()
                .role(newUser.getRole().getRole())
                .build();
    }

    @Transactional
    public UserMeetingTimeResponseDto createHostTime(
            String url,
            Long userId,
            List<UserMeetingTimeSaveRequestDto> requestDtoList
    ) {
        User host = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        createMeetingTimeList(host, requestDtoList);
        String accessToken = jwtService.issuedToken(host.getId().toString());
        return new UserMeetingTimeResponseDto(url, accessToken);
    }

    @Transactional
    public void createMeetingTimeList(
            User user,
            List<UserMeetingTimeSaveRequestDto> requestDtoList
    ) {
        isDuplicatedDate(requestDtoList);
        List<MeetingTime> meetingTimeList = requestDtoList
                .stream()
                .map(userMeetingTimeSaveRequestDto -> MeetingTime.newInstance(user,
                        userMeetingTimeSaveRequestDto.getPriority(),
                        userMeetingTimeSaveRequestDto.getMonth(),
                        userMeetingTimeSaveRequestDto.getDay(),
                        userMeetingTimeSaveRequestDto.getDayOfWeek(),
                        userMeetingTimeSaveRequestDto.getStartTime(),
                        userMeetingTimeSaveRequestDto.getEndTime()))
                .collect(Collectors.toList());
        meetingTimeRepository.saveAllAndFlush(meetingTimeList);
    }

    public void isDuplicatedDate(List<UserMeetingTimeSaveRequestDto> requestDtoList) {
        Map<String, List<TimeSlot>> meetingTimeAvailable = new HashMap<>();
        for(UserMeetingTimeSaveRequestDto requestDto: requestDtoList){
            String col = String.format("%s %s %s", requestDto.getMonth(), requestDto.getDay(), requestDto.getDayOfWeek());
            List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(requestDto.getStartTime().ordinal(), requestDto.getEndTime().ordinal());
            if (meetingTimeAvailable.containsKey(col)) {
                if (meetingTimeAvailable.containsKey(col)) {
                    if(meetingTimeAvailable.get(col).stream().anyMatch(timeSlots::contains)){
                        throw new BadRequestException(Error.DUPLICATED_TIME_EXCEPTION);
                    }
                }
            }else{
                meetingTimeAvailable.put(col, timeSlots);
            }
        }
    }
}
