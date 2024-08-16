package com.asap.server.persistence.repository.timeblock;

import com.asap.server.persistence.config.querydsl.QueryDslConfig;
import com.asap.server.persistence.domain.AvailableDate;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.Place;
import com.asap.server.persistence.domain.TimeBlock;
import com.asap.server.persistence.domain.TimeBlockUser;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.domain.enums.TimeSlot;
import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimeBlockRepositoryImplTest {
    @Autowired
    EntityManager em;
    @Autowired
    TimeBlockRepository timeBlockRepositoryCustom;

    @Test
    @DisplayName("2명의 사용자 모두 2개의 요일에 6_00 ~ 8_00까지 시간을 입력했을 때, 총 10개의 레코드를 불러온다.")
    void findAllTimeBlockByMeetingTest() {
        // given
        Place place = Place.builder()
                .placeType(PlaceType.OFFLINE)
                .build();
        Meeting meeting = Meeting.builder()
                .title("회의 테스트")
                .password("0000")
                .additionalInfo("")
                .duration(Duration.HALF)
                .place(place)
                .build();
        em.persist(meeting);

        final Name name = new Name("KWY");
        final Name name2 = new Name("DSY");
        User user = User.builder()
                .meeting(meeting)
                .name(name)
                .role(Role.HOST)
                .build();
        User user2 = User.builder()
                .meeting(meeting)
                .name(name2)
                .role(Role.MEMBER)
                .build();
        em.persist(user);
        em.persist(user2);

        AvailableDate availableDate = AvailableDate.builder()
                .date(LocalDate.of(2024, 7, 9))
                .meeting(meeting)
                .build();
        AvailableDate availableDate2 = AvailableDate.builder()
                .date(LocalDate.of(2024, 7, 10))
                .meeting(meeting)
                .build();
        em.persist(availableDate);
        em.persist(availableDate2);

        List<TimeBlock> timeBlocks = createTimeBlocks(availableDate, TimeSlot.SLOT_6_00, TimeSlot.SLOT_8_00);
        List<TimeBlock> timeBlocks2 = createTimeBlocks(availableDate2, TimeSlot.SLOT_6_00, TimeSlot.SLOT_8_00);
        List<TimeBlockUser> tbu = createTimeBlockUsers(timeBlocks, user);
        List<TimeBlockUser> tbu2 = createTimeBlockUsers(timeBlocks2, user);
        List<TimeBlockUser> tbu3 = createTimeBlockUsers(timeBlocks, user2);
        List<TimeBlockUser> tbu4 = createTimeBlockUsers(timeBlocks2, user2);

        // when
        List<TimeBlockDto> response = timeBlockRepositoryCustom.findAllTimeBlockByMeeting(meeting.getId());

        // then
        assertThat(response.size()).isEqualTo(10);
    }

    private List<TimeBlock> createTimeBlocks(AvailableDate availableDate, TimeSlot startTime, TimeSlot endTime) {
        List<TimeSlot> timeSlots = TimeSlot.getTimeSlots(startTime.ordinal(), endTime.ordinal());
        List<TimeBlock> timeBlocks = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            final TimeBlock timeBlock = TimeBlock.builder()
                    .availableDate(availableDate)
                    .timeSlot(timeSlot)
                    .build();
            em.persist(timeBlock);
            timeBlocks.add(timeBlock);
        }
        return timeBlocks;
    }

    private List<TimeBlockUser> createTimeBlockUsers(List<TimeBlock> timeBlocks, User user) {
        List<TimeBlockUser> timeBlockUsers = new ArrayList<>();
        for (TimeBlock timeBlock : timeBlocks) {
            TimeBlockUser tbu = TimeBlockUser.builder()
                    .timeBlock(timeBlock)
                    .user(user)
                    .build();
            em.persist(tbu);
            timeBlockUsers.add(tbu);
        }
        return timeBlockUsers;
    }
}