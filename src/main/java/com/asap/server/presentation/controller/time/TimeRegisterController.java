package com.asap.server.presentation.controller.time;

import com.asap.server.common.exception.Success;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.presentation.config.resolver.user.UserId;
import com.asap.server.presentation.controller.dto.request.AvailableTimeRequestDto;
import com.asap.server.presentation.controller.dto.request.UserMeetingTimeSaveRequestDto;
import com.asap.server.presentation.controller.dto.response.UserMeetingTimeResponseDto;
import com.asap.server.presentation.controller.dto.response.UserTimeResponseDto;
import com.asap.server.presentation.controller.time.docs.TimeRegisterControllerDocs;
import com.asap.server.service.UserService;
import com.asap.server.service.dto.UserMeetingScheduleRegisterDto;
import com.asap.server.service.dto.UserTimeRegisterDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class TimeRegisterController implements TimeRegisterControllerDocs {
    private final UserService userService;

    @PostMapping("/host/{meetingId}/time")
    @Override
    public SuccessResponse<UserMeetingTimeResponseDto> createHostTime(
            @MeetingPathVariable final Long meetingId,
            @RequestBody final List<@Valid @NotNull UserMeetingTimeSaveRequestDto> requestDtoList,
            @UserId final Long userId
    ) {
        return SuccessResponse.success(
                Success.CREATE_HOST_TIME_SUCCESS,
                userService.createHostTime(meetingId, userId, UserMeetingScheduleRegisterDto.of(requestDtoList))
        );
    }

    @PostMapping("/{meetingId}/time")
    @Override
    public SuccessResponse<UserTimeResponseDto> createMemberTime(
            @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final AvailableTimeRequestDto requestDto
    ) {
        return SuccessResponse.success(
                Success.CREATE_MEETING_TIME_SUCCESS,
                userService.createUserTime(meetingId, UserTimeRegisterDto.of(requestDto))
        );
    }
}
