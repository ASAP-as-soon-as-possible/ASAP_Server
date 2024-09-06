package com.asap.server.persistence.repository;

import com.asap.server.persistence.config.querydsl.QueryDslConfig;
import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.Place;
import com.asap.server.persistence.domain.user.Name;
import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.domain.enums.Duration;
import com.asap.server.persistence.domain.enums.PlaceType;
import com.asap.server.persistence.domain.enums.Role;
import com.asap.server.persistence.repository.meeting.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryCustomTest {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("방장이 로그인을 할 때, 방장 정보도 함께 불러온다.")
    void fetchJoinTest() {
        final Place place = Place.builder()
                .placeType(PlaceType.OFFLINE)
                .build();

        final Meeting meeting = Meeting.builder()
                .title("회의 테스트")
                .password("0000")
                .additionalInfo("")
                .duration(Duration.HALF)
                .place(place)
                .build();

        final Name name = new Name("강원용");
        final User user = User.builder()
                .meeting(meeting)
                .name(name)
                .role(Role.HOST)
                .isFixed(false)
                .build();
        meeting.setHost(user);

        em.persist(meeting);
        em.persist(user);
        em.flush();
        em.clear();

        // when
        Meeting result = meetingRepository.findByIdWithHost(meeting.getId()).get();
        User host = result.getHost();

        // then
        assertThat(host).isNotNull();
    }

}
