package com.asap.server.service;

import com.asap.server.common.utils.BestMeetingUtil;
import com.asap.server.common.utils.DateUtil;
import com.asap.server.config.jwt.JwtService;
import com.asap.server.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.controller.dto.response.AvailableDatesDto;
import com.asap.server.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.controller.dto.response.MeetingTitleResponseDto;
import com.asap.server.controller.dto.response.TimeTableResponseDto;
import com.asap.server.domain.ConfirmedDateTime;
import com.asap.server.domain.Meeting;
import com.asap.server.domain.Place;
import com.asap.server.domain.User;
import com.asap.server.domain.enums.Role;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.ConflictException;
import com.asap.server.exception.model.ForbiddenException;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import com.asap.server.repository.MeetingRepository;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.TimeBlocksByDateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.asap.server.exception.Error.INVALID_MEETING_HOST_EXCEPTION;
import static com.asap.server.exception.Error.MEETING_VALIDATION_FAILED_EXCEPTION;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final AvailableDateService availableDateService;
    private final PreferTimeService preferTimeService;
    private final JwtService jwtService;
    private final BestMeetingUtil bestMeetingUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MeetingSaveResponseDto create(final MeetingSaveRequestDto meetingSaveRequestDto) {
        String encryptedPassword = passwordEncoder.encode(meetingSaveRequestDto.getPassword());

        Meeting meeting = Meeting.builder()
                .title(meetingSaveRequestDto.getTitle())
                .password(encryptedPassword)
                .additionalInfo(meetingSaveRequestDto.getAdditionalInfo())
                .duration(meetingSaveRequestDto.getDuration())
                .place(
                        Place.builder()
                                .placeType(meetingSaveRequestDto.getPlace())
                                .placeDetail(meetingSaveRequestDto.getPlaceDetail())
                                .build())
                .build();

        meetingRepository.save(meeting);

        User host = userService.createUser(meeting, meetingSaveRequestDto.getName(), Role.HOST);

        preferTimeService.create(meeting, meetingSaveRequestDto.getPreferTimes());
        availableDateService.create(meeting, meetingSaveRequestDto.getAvailableDates());

        meeting.setHost(host);

        String accessToken = jwtService.issuedToken(host.getId().toString());
        meeting.setUrl(Base64Utils.encodeToUrlSafeString(meeting.getId().toString().getBytes()));

        return MeetingSaveResponseDto.builder()
                .url(meeting.getUrl())
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public void confirmMeeting(
            final MeetingConfirmRequestDto meetingConfirmRequestDto,
            final Long meetingId,
            final Long userId
    ) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId))
            throw new UnauthorizedException(INVALID_MEETING_HOST_EXCEPTION);

        userService.setFixedUsers(meeting, meetingConfirmRequestDto.getUsers());

        LocalDate fixedDate = DateUtil.transformLocalDate(meetingConfirmRequestDto.getMonth(), meetingConfirmRequestDto.getDay());

        LocalTime startTime = DateUtil.parseLocalTime(meetingConfirmRequestDto.getStartTime().getTime());
        LocalTime endTime = DateUtil.parseLocalTime(meetingConfirmRequestDto.getEndTime().getTime());

        LocalDateTime fixedStartDateTime = LocalDateTime.of(fixedDate, startTime);
        LocalDateTime fixedEndDateTime = LocalDateTime.of(fixedDate, endTime);

        meeting.setConfirmedDateTime(fixedStartDateTime, fixedEndDateTime);

        deleteMeetingTimes(meeting);
    }

    private void deleteMeetingTimes(final Meeting meeting) {
        availableDateService.deleteUserTimes(meeting);
        preferTimeService.deletePreferTimes(meeting);
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponseDto getMeetingSchedule(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));
        if (meeting.isConfirmedMeeting())
            throw new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION);


        return MeetingScheduleResponseDto.builder()
                .duration(meeting.getDuration())
                .place(meeting.getPlace().getPlaceType())
                .placeDetail(meeting.getPlace().getPlaceDetail())
                .availableDates(availableDateService.getAvailableDates(meeting))
                .preferTimes(preferTimeService.getPreferTimes(meeting))
                .build();
    }

    public FixedMeetingResponseDto getFixedMeetingInformation(final Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.isConfirmedMeeting())
            throw new ForbiddenException(Error.MEETING_NOT_CONFIRMED_EXCEPTION);

        List<String> fixedUserNames = userService.getFixedUsers(meeting);

        ConfirmedDateTime confirmedDateTime = meeting.getConfirmedDateTime();

        return FixedMeetingResponseDto
                .builder()
                .title(meeting.getTitle())
                .place(meeting.getPlace().getPlaceType().getPlace())
                .placeDetail(meeting.getPlace().getPlaceDetail())
                .month(DateUtil.getMonth(confirmedDateTime.getConfirmedStartTime()))
                .day(DateUtil.getDay(confirmedDateTime.getConfirmedStartTime()))
                .dayOfWeek(DateUtil.getDayOfWeek(confirmedDateTime.getConfirmedStartTime()))
                .startTime(DateUtil.getTime(confirmedDateTime.getConfirmedStartTime()))
                .endTime(DateUtil.getTime(confirmedDateTime.getConfirmedEndTime()))
                .hostName(meeting.getHost().getName())
                .userNames(fixedUserNames)
                .additionalInfo(meeting.getAdditionalInfo())
                .build();
    }

    public TimeTableResponseDto getTimeTable(final Long userId, final Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId))
            throw new UnauthorizedException(INVALID_MEETING_HOST_EXCEPTION);

        List<String> memberNames = userService.findUserNameByMeeting(meeting);

        List<AvailableDatesDto> availableDatesDtos = availableDateService.findAvailableDateByMeeting(meeting).stream()
                .map(availableDate -> availableDateService.getAvailableDatesDto(availableDate, memberNames.size()))
                .collect(Collectors.toList());

        return TimeTableResponseDto.builder()
                .totalUserNames(memberNames)
                .memberCount(memberNames.size())
                .availableDateTimes(availableDatesDtos)
                .build();
    }

    public MeetingTitleResponseDto getIsFixedMeeting(final Long meetingId) throws ConflictException {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (meeting.isConfirmedMeeting())
            throw new ConflictException(Error.MEETING_VALIDATION_FAILED_EXCEPTION);

        return MeetingTitleResponseDto.builder()
                .title(meeting.getTitle())
                .build();
    }

    public BestMeetingTimeResponseDto getBestMeetingTime(final Long meetingId, final Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId)) throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);

        int userCount = userService.getMeetingUserCount(meeting);
        List<TimeBlocksByDateVo> availableDates = availableDateService.getAvailableDateVos(meeting);

        List<BestMeetingTimeVo> bestMeetingTimes = bestMeetingUtil.getBestMeetingTime(availableDates, meeting.getDuration(), userCount);
        return BestMeetingTimeResponseDto.of(userCount, bestMeetingTimes);
    }

}
