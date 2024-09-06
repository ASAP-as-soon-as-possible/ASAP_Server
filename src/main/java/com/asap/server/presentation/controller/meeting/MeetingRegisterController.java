package com.asap.server.presentation.controller.meeting;

import com.asap.server.common.exception.Success;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.presentation.config.resolver.user.UserId;
import com.asap.server.presentation.controller.dto.request.MeetingConfirmRequestDto;
import com.asap.server.presentation.controller.dto.request.MeetingSaveRequestDto;
import com.asap.server.presentation.controller.dto.response.MeetingSaveResponseDto;
import com.asap.server.presentation.controller.meeting.docs.MeetingRegisterControllerDocs;
import com.asap.server.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingRegisterController implements MeetingRegisterControllerDocs {
    private final MeetingService meetingService;

    @PostMapping
    @Override
    public SuccessResponse<MeetingSaveResponseDto> create(
            @RequestBody @Valid final MeetingSaveRequestDto meetingSaveRequestDto
    ) {
        return SuccessResponse.success(Success.CREATE_MEETING_SUCCESS, meetingService.create(meetingSaveRequestDto));
    }

    @PostMapping("/{meetingId}/confirm")
    @Override
    public SuccessResponse confirmMeeting(
            @MeetingPathVariable final Long meetingId,
            @RequestBody @Valid final MeetingConfirmRequestDto meetingConfirmRequestDto,
            @UserId final Long userId
    ) {
        meetingService.confirmMeeting(meetingConfirmRequestDto, meetingId, userId);
        return SuccessResponse.success(Success.CONFIRM_MEETING_SUCCESS);
    }
}
