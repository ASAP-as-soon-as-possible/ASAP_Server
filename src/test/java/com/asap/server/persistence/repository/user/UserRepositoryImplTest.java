package com.asap.server.persistence.repository.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.asap.server.persistence.config.querydsl.QueryDslConfig;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.Place;
import com.asap.server.persistence.domain.User;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
import com.asap.server.persistence.domain.enums.Role;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryImplTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("KWY, DSY, LJH, SES 중 KWY, DSY만 확정된 회의에 참여한다면, KWY, DSY의 is_fixed 칼럼을 1로 수정한다.")
    void updateUserIsFixedByMeetingTest() {
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

        User user = User.builder()
                .meeting(meeting)
                .name("KWY")
                .isFixed(false)
                .role(Role.HOST)
                .build();
        User user2 = User.builder()
                .meeting(meeting)
                .name("DSY")
                .isFixed(false)
                .role(Role.MEMBER)
                .build();
        User user3 = User.builder()
                .meeting(meeting)
                .name("LJH")
                .isFixed(false)
                .role(Role.MEMBER)
                .build();
        User user4 = User.builder()
                .meeting(meeting)
                .name("SES")
                .isFixed(false)
                .role(Role.MEMBER)
                .build();
        em.persist(user);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.flush();
        em.clear();

        // when
        userRepository.updateUserIsFixedByMeeting(
                meeting,
                List.of(
                        user.getId(),
                        user2.getId()
                )
        );

        // then
        User result = em.find(User.class, user.getId());
        User result2 = em.find(User.class, user2.getId());
        User result3 = em.find(User.class, user3.getId());
        User result4 = em.find(User.class, user4.getId());

        assertThat(result.getIsFixed()).isTrue();
        assertThat(result2.getIsFixed()).isTrue();
        assertThat(result3.getIsFixed()).isFalse();
        assertThat(result4.getIsFixed()).isFalse();
    }
}