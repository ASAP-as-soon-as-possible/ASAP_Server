package com.asap.server.presentation.controller.meeting;

import com.asap.server.common.exception.Success;
import com.asap.server.presentation.common.dto.SuccessResponse;
import com.asap.server.presentation.config.resolver.meeting.MeetingPathVariable;
import com.asap.server.presentation.config.resolver.user.UserId;
import com.asap.server.presentation.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.presentation.controller.dto.response.FixedMeetingResponseDto;
import com.asap.server.presentation.controller.dto.response.MeetingScheduleResponseDto;
import com.asap.server.presentation.controller.dto.response.MeetingTitleResponseDto;
import com.asap.server.presentation.controller.dto.response.TimeTableResponseDto;
import com.asap.server.presentation.controller.meeting.docs.MeetingRetrieveControllerDocs;
import com.asap.server.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingRetrieveController implements MeetingRetrieveControllerDocs {
    private final MeetingService meetingService;

    @GetMapping("/{meetingId}/schedule")
    @Override
    public SuccessResponse<MeetingScheduleResponseDto> getMeetingSchedule(
            @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(
                Success.FIND_MEETING_SCHEDULE_SUCCESS,
                meetingService.getMeetingSchedule(meetingId)
        );
    }

    @GetMapping("/{meetingId}/card")
    @Override
    public SuccessResponse<FixedMeetingResponseDto> getFixedMeetingInformation(
            @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(
                Success.FIXED_MEETING_SUCCESS,
                meetingService.getFixedMeetingInformation(meetingId)
        );
    }

    @GetMapping("/{meetingId}/timetable")
    @Override
    public SuccessResponse<TimeTableResponseDto> getTimeTable(
            @MeetingPathVariable final Long meetingId,
            @UserId final Long userId
    ) {
        return SuccessResponse.success(Success.FIND_TIME_TABLE_SUCCESS, meetingService.getTimeTable(userId, meetingId));
    }

    @GetMapping("/{meetingId}")
    @Override
    public SuccessResponse<MeetingTitleResponseDto> getIsFixedMeeting(
            @MeetingPathVariable final Long meetingId
    ) {
        return SuccessResponse.success(Success.MEETING_VALIDATION_SUCCESS, meetingService.getIsFixedMeeting(meetingId));
    }

    @GetMapping("/{meetingId}/details")
    @Override
    public SuccessResponse<BestMeetingTimeResponseDto> getBestMeetingTime(
            @MeetingPathVariable final Long meetingId,
            @UserId Long userId
    ) {
        return SuccessResponse.success(
                Success.BEST_MEETING_SUCCESS,
                meetingService.getBestMeetingTime(meetingId, userId)
        );
    }
}
