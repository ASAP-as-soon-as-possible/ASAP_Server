package com.asap.server.service.user;

import static com.asap.server.common.exception.Error.MEETING_VALIDATION_FAILED_EXCEPTION;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.HostTimeForbiddenException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.common.jwt.JwtService;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.presentation.controller.dto.response.HostLoginResponseDto;
import com.asap.server.service.TimeBlockUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final MeetingRepository meetingRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TimeBlockUserService timeBlockUserService;

    @Transactional
    public HostLoginResponseDto loginByHost(
            final Long meetingId,
            final String name,
            final String password
    ) {
        Meeting meeting = meetingRepository.findByIdWithHost(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.checkHostName(name)) {
            throw new UnauthorizedException(Error.INVALID_HOST_ID_PASSWORD_EXCEPTION);
        }

        if (!passwordEncoder.matches(password, meeting.getPassword())) {
            throw new UnauthorizedException(Error.INVALID_HOST_ID_PASSWORD_EXCEPTION);
        }

        if (meeting.isConfirmedMeeting()) {
            throw new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION);
        }

        HostLoginResponseDto responseDto = new HostLoginResponseDto(
                jwtService.issuedToken(meeting.getHost().getId().toString())
        );

        if (timeBlockUserService.isEmptyHostTimeBlock(meeting.getHost())) {
            throw new HostTimeForbiddenException(Error.HOST_MEETING_TIME_NOT_PROVIDED, responseDto);
        }

        return responseDto;
    }
}
