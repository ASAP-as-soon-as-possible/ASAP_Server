package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.domain.MeetingTime;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.Role;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.repository.MeetingTimeRepository;
import com.asap.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public UserMeetingTimeResponseDto createHostTime(String url, Long userId, List<UserMeetingTimeSaveRequestDto> requestDtoList) {
        User host = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        List<MeetingTime> meetingTimeList = requestDtoList
                .stream()
                .map(userMeetingTimeSaveRequestDto -> MeetingTime.newInstance(host,
                        userMeetingTimeSaveRequestDto.getPriority(),
                        userMeetingTimeSaveRequestDto.getMonth(),
                        userMeetingTimeSaveRequestDto.getDay(),
                        userMeetingTimeSaveRequestDto.getDayOfWeek(),
                        userMeetingTimeSaveRequestDto.getStartTime(),
                        userMeetingTimeSaveRequestDto.getEndTime()))
                .collect(Collectors.toList());
        meetingTimeRepository.saveAllAndFlush(meetingTimeList);
        String accessToken = jwtService.issuedToken(host.getId().toString());
        return new UserMeetingTimeResponseDto(url, accessToken);
    }
}
