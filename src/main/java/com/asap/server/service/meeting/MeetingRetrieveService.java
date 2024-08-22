package com.asap.server.service.meeting;

import static com.asap.server.common.exception.Error.MEETING_VALIDATION_FAILED_EXCEPTION;

import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.ConflictException;
import com.asap.server.common.exception.model.NotFoundException;
import com.asap.server.common.exception.model.UnauthorizedException;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.presentation.controller.dto.response.BestMeetingTimeResponseDto;
import com.asap.server.service.UserService;
import com.asap.server.service.meeting.recommend.MeetingTimeRecommendService;
import com.asap.server.service.time.UserMeetingScheduleService;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.user.UserRetrieveService;
import com.asap.server.service.vo.BestMeetingTimeVo;
import com.asap.server.service.vo.BestMeetingTimeWithUsersVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingRetrieveService {
    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final MeetingTimeRecommendService meetingTimeRecommendService;
    private final UserMeetingScheduleService userMeetingScheduleService;

    public BestMeetingTimeResponseDto getBestMeetingTime(final Long meetingId, final Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(Error.MEETING_NOT_FOUND_EXCEPTION));

        if (!meeting.authenticateHost(userId)) {
            throw new UnauthorizedException(Error.INVALID_MEETING_HOST_EXCEPTION);
        }
        if (meeting.isConfirmedMeeting()) {
            throw new ConflictException(MEETING_VALIDATION_FAILED_EXCEPTION);
        }

        int userCount = userService.getMeetingUserCount(meeting);

        List<TimeBlockVo> timeBlocks = userMeetingScheduleService.getTimeBlocks(meetingId);

        List<BestMeetingTimeVo> bestMeetingTimes = meetingTimeRecommendService.getBestMeetingTime(
                timeBlocks,
                meeting.getDuration(),
                userCount
        );
        List<BestMeetingTimeWithUsersVo> bestMeetingTimeWithUsers = userService.getBestMeetingInUsers(
                meetingId,
                bestMeetingTimes
        );
        return BestMeetingTimeResponseDto.of(userCount, bestMeetingTimeWithUsers);
    }
}
