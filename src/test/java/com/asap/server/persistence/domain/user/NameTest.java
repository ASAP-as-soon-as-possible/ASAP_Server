package com.asap.server.persistence.domain.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.asap.server.common.exception.model.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @DisplayName("이름 값이 8자를 초과하면 BadRequestException 을 반환한다.")
    @Test
    void test() {
        // when, then
        assertThatThrownBy(() -> {
            new Name(null);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 이름에는 null이 들어올 수 없습니다.");
    }

    @DisplayName("이름 값이 8자를 초과하면 BadRequestException 을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void test2(String value) {
        // when, then
        assertThatThrownBy(() -> {
            new Name(value);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 이름에는 빈 값이 들어올 수 없습니다.");
    }

    @DisplayName("이름 값이 8자를 초과하면 BadRequestException 을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"KWY이름8자초과", "DSH이름8자초과"})
    void test3(String value) {
        // when, then
        assertThatThrownBy(() -> {
            new Name(value);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("사용자 이름의 최대 입력 길이(8자)를 초과했습니다.");
    }

    @DisplayName("이름 값 앞 뒤로 공백을 제거한 값을 저장한다.")
    @ParameterizedTest
    @ValueSource(strings = {"   KWY", "KWY   ", "  KWY  "})
    void test4(String value) {
        // when
        Name name = new Name(value);

        // then
        assertThat(name.getValue()).isEqualTo("KWY");
    }
}