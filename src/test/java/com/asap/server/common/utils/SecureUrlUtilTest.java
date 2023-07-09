package com.asap.server.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SecureUrlUtilTest {

    private SecureUrlUtil secureUrlUtil;

    @BeforeEach
    void setup() {
        secureUrlUtil = new SecureUrlUtil();
    }

    @Test
    @DisplayName("meeting id 인코딩 디코딩 테스트")
    void secureTest1() {
        // given
        Long meetingId = 3L;

        // when
        String url = secureUrlUtil.encodeUrl(meetingId);
        Long result = secureUrlUtil.decodeUrl(url);

        // then
        assertThat(result).isEqualTo(3L);
    }
}
