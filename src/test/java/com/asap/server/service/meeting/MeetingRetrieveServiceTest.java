package com.asap.server.service.meeting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.asap.server.common.utils.DateUtil;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import com.asap.server.service.meeting.dto.BestMeetingTimeDto;
import com.asap.server.service.meeting.dto.UserDto;
import com.asap.server.service.time.MeetingTimeRecommendService;
import com.asap.server.service.time.UserMeetingScheduleService;
import com.asap.server.service.time.dto.retrieve.AvailableDatesRetrieveDto;
import com.asap.server.service.time.dto.retrieve.TimeBlockRetrieveDto;
import com.asap.server.service.time.dto.retrieve.TimeTableRetrieveDto;
import com.asap.server.service.time.vo.TimeBlockVo;
import com.asap.server.service.user.UserRetrieveService;
import com.asap.server.service.time.vo.BestMeetingTimeVo;
import com.asap.server.service.time.vo.BestMeetingTimeWithUsers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeetingRetrieveServiceTest {
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private UserRetrieveService userRetrieveService;
    @Mock
    private MeetingTimeRecommendService meetingTimeRecommendService;
    @Mock
    private UserMeetingScheduleService userMeetingScheduleService;
    @InjectMocks
    private MeetingRetrieveService meetingRetrieveService;

    @Nested
    @DisplayName("정상 응답 테스트")
    class SuccessTestCase {
        @DisplayName("추천된 회의 시간이 1가지 일 때, (회의 시간, null, null)을 반환한다.")
        @Test
        void test() {
            // given
            Meeting meeting = Meeting.builder()
                    .id(1L)
                    .host(User.builder().id(1L).build())
                    .duration(Duration.HALF)
                    .build();
            User user = User.builder()
                    .id(1L)
                    .name(new Name("KWY"))
                    .build();
            User user2 = User.builder()
                    .id(2L)
                    .name(new Name("DSH"))
                    .build();
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L))
            );
            when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
            when(userRetrieveService.getMeetingUserCount(meeting)).thenReturn(2);
            when(userMeetingScheduleService.getTimeBlocks(1L)).thenReturn(timeBlocks);
            when(meetingTimeRecommendService.getBestMeetingTime(timeBlocks, meeting.getDuration(), 2)).thenReturn(
                    Arrays.asList(
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_12_00, TimeSlot.SLOT_12_30, 0,
                                    List.of(1L, 2L)),
                            null,
                            null
                    )
            );
            when(userRetrieveService.getUserIdToUserMap(1L)).thenReturn(Map.of(1L, user, 2L, user2));

            List<BestMeetingTimeWithUsers> bestMeetingTimeWithUsers = Arrays.asList(
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_12_00,
                            TimeSlot.SLOT_12_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    ),
                    null,
                    null
            );
            BestMeetingTimeDto expected = BestMeetingTimeDto.of(2, bestMeetingTimeWithUsers);

            // when
            BestMeetingTimeDto result = meetingRetrieveService.getBestMeetingTime(1L, 1L);

            // then
            assertThat(expected).isEqualTo(result);
        }

        @DisplayName("추천된 회의 시간이 2가지 일 때, (회의 시간1, 회의 시간2, null)을 반환한다.")
        @Test
        void test2() {
            // given
            Meeting meeting = Meeting.builder()
                    .id(1L)
                    .host(User.builder().id(1L).build())
                    .duration(Duration.HALF)
                    .build();
            User user = User.builder()
                    .id(1L)
                    .name(new Name("KWY"))
                    .build();
            User user2 = User.builder()
                    .id(2L)
                    .name(new Name("DSH"))
                    .build();
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L))
            );
            when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
            when(userRetrieveService.getMeetingUserCount(meeting)).thenReturn(2);
            when(userMeetingScheduleService.getTimeBlocks(1L)).thenReturn(timeBlocks);
            when(meetingTimeRecommendService.getBestMeetingTime(timeBlocks, meeting.getDuration(), 2)).thenReturn(
                    Arrays.asList(
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_12_00, TimeSlot.SLOT_12_30, 0,
                                    List.of(1L, 2L)),
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_13_00, TimeSlot.SLOT_13_30, 0,
                                    List.of(1L, 2L)),
                            null
                    )
            );
            when(userRetrieveService.getUserIdToUserMap(1L)).thenReturn(Map.of(1L, user, 2L, user2));

            List<BestMeetingTimeWithUsers> bestMeetingTimeWithUsers = Arrays.asList(
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_12_00,
                            TimeSlot.SLOT_12_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    ),
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_13_00,
                            TimeSlot.SLOT_13_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    ),
                    null
            );
            BestMeetingTimeDto expected = BestMeetingTimeDto.of(2, bestMeetingTimeWithUsers);

            // when
            BestMeetingTimeDto result = meetingRetrieveService.getBestMeetingTime(1L, 1L);

            // then
            assertThat(expected).isEqualTo(result);
        }

        @DisplayName("추천된 회의 시간이 3가지 일 때, (회의 시간1, 회의 시간2, 회의 시간3)을 반환한다.")
        @Test
        void test3() {
            // given
            Meeting meeting = Meeting.builder()
                    .id(1L)
                    .host(User.builder().id(1L).build())
                    .duration(Duration.HALF)
                    .build();
            User user = User.builder()
                    .id(1L)
                    .name(new Name("KWY"))
                    .build();
            User user2 = User.builder()
                    .id(2L)
                    .name(new Name("DSH"))
                    .build();
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_12_00, 0, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_13_00, 0, List.of(1L, 2L)),
                    new TimeBlockVo(LocalDate.of(2024, 7, 10), TimeSlot.SLOT_14_00, 0, List.of(1L, 2L))
            );
            when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
            when(userRetrieveService.getMeetingUserCount(meeting)).thenReturn(2);
            when(userMeetingScheduleService.getTimeBlocks(1L)).thenReturn(timeBlocks);
            when(meetingTimeRecommendService.getBestMeetingTime(timeBlocks, meeting.getDuration(), 2)).thenReturn(
                    Arrays.asList(
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_12_00, TimeSlot.SLOT_12_30, 0,
                                    List.of(1L, 2L)),
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_13_00, TimeSlot.SLOT_13_30, 0,
                                    List.of(1L, 2L)),
                            new BestMeetingTimeVo(LocalDate.of(2024, 7, 9), TimeSlot.SLOT_14_00, TimeSlot.SLOT_14_30, 0,
                                    List.of(1L, 2L))
                    )
            );
            when(userRetrieveService.getUserIdToUserMap(1L)).thenReturn(Map.of(1L, user, 2L, user2));

            List<BestMeetingTimeWithUsers> bestMeetingTimeWithUsers = Arrays.asList(
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_12_00,
                            TimeSlot.SLOT_12_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    ),
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_13_00,
                            TimeSlot.SLOT_13_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    ),
                    new BestMeetingTimeWithUsers(
                            LocalDate.of(2024, 7, 9),
                            TimeSlot.SLOT_14_00,
                            TimeSlot.SLOT_14_30,
                            0,
                            List.of(
                                    new UserDto(1L, "KWY"),
                                    new UserDto(2L, "DSH")
                            )
                    )
            );
            BestMeetingTimeDto expected = BestMeetingTimeDto.of(2, bestMeetingTimeWithUsers);

            // when
            BestMeetingTimeDto result = meetingRetrieveService.getBestMeetingTime(1L, 1L);

            // then
            assertThat(expected).isEqualTo(result);
        }
    }

    @Nested
    @DisplayName("회의 종합 일정 시간표 테스트")
    class MeetingTimeTableTest {

        @Test
        @DisplayName("각 참여자 수에 맞는 colorLevel을 반환한다.")
        void testTimeTable() {

            // given
            Meeting meeting = Meeting.builder()
                    .id(1L)
                    .host(User.builder().id(1L).build())
                    .duration(Duration.HALF)
                    .build();
            User user = User.builder()
                    .id(1L)
                    .name(new Name("KWY"))
                    .build();
            User user2 = User.builder()
                    .id(2L)
                    .name(new Name("DSH"))
                    .build();
            User user3 = User.builder()
                    .id(3L)
                    .name(new Name("SJW"))
                    .build();
            User user4 = User.builder()
                    .id(4L)
                    .name(new Name("SCW"))
                    .build();
            User user5 = User.builder()
                    .id(5L)
                    .name(new Name("KTH"))
                    .build();
            LocalDate date = LocalDate.of(2024, 7, 9);
            List<TimeBlockVo> timeBlocks = List.of(
                    new TimeBlockVo(date, TimeSlot.SLOT_12_00, 0, List.of(1L, 2L, 3L, 4L, 5L)),
                    new TimeBlockVo(date, TimeSlot.SLOT_13_00, 0, List.of(1L, 2L, 3L, 4L)),
                    new TimeBlockVo(date, TimeSlot.SLOT_14_00, 0, List.of(1L, 2L, 3L)),
                    new TimeBlockVo(date, TimeSlot.SLOT_15_00, 0, List.of(1L, 2L)),
                    new TimeBlockVo(date, TimeSlot.SLOT_16_00, 0, List.of(1L))
            );

            when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
            when(userMeetingScheduleService.getTimeBlocks(1L)).thenReturn(timeBlocks);
            when(userRetrieveService.getUserIdToUserMap(1L)).thenReturn(Map.of(1L, user, 2L, user2, 3L, user3, 4L, user4, 5L, user5));


            List<TimeBlockRetrieveDto> expectedTimeSlotDto = List.of(
                    new TimeBlockRetrieveDto("12:00", List.of("KWY", "DSH", "SJW", "SCW", "KTH"), 5),
                    new TimeBlockRetrieveDto("13:00", List.of("KWY", "DSH", "SJW", "SCW"), 4),
                    new TimeBlockRetrieveDto("14:00", List.of("KWY", "DSH", "SJW"), 3),
                    new TimeBlockRetrieveDto("15:00", List.of("KWY", "DSH"), 2),
                    new TimeBlockRetrieveDto("16:00", List.of("KWY"), 1)
            );

            List<AvailableDatesRetrieveDto> expectedAvailableDto = List.of(
                    new AvailableDatesRetrieveDto(
                            DateUtil.getMonth(date),
                            DateUtil.getDay(date),
                            DateUtil.getDayOfWeek(date),
                            expectedTimeSlotDto
                    )
            );
            TimeTableRetrieveDto expected = TimeTableRetrieveDto.of(
                    List.of("KWY", "DSH", "SJW", "SCW", "KTH"),
                    expectedAvailableDto
            );

            TimeTableRetrieveDto result = meetingRetrieveService.getTimeTable(1L, 1L);

            assertThat(result).isEqualTo(expected);
        }

    }
}