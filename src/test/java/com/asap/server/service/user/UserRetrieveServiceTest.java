package com.asap.server.service.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.asap.server.persistence.domain.user.User;
import com.asap.server.persistence.repository.user.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRetrieveServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserRetrieveService userRetrieveService;

    @Test
    @DisplayName("사용자가 존재할 때, userId를 키로 갖는 Map<Long, User>을 반환한다.")
    void test() {
        // given
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build(),
                User.builder().id(3L).build()
        );
        when(userRepository.findAllByMeetingId(1L)).thenReturn(users);
        Map<Long, User> expected = Map.of(
                1L, User.builder().id(1L).build(),
                2L, User.builder().id(2L).build(),
                3L, User.builder().id(3L).build()
        );

        // when
        Map<Long, User> result = userRetrieveService.getUserIdToUserMap(1L);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("사용자가 없을 때, 빈 Map을 반환한다.")
    void test2() {
        // given
        when(userRepository.findAllByMeetingId(1L)).thenReturn(Collections.emptyList());
        Map<Long, User> expected = Map.of();

        // when
        Map<Long, User> result = userRetrieveService.getUserIdToUserMap(1L);

        // then
        assertThat(result).isEqualTo(expected);
    }
}