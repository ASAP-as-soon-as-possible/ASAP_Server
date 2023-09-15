package com.asap.server.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordEncryptionUtilTest {

    @Test
    @DisplayName("password 암호화 테스트")
    public void test() {
        // given
        String password = "1234";
        String salt = "salt";
        String result = "4b3bed8af7b7612e8c1e25f63ba24496f5b16b2df44efb2db7ce3cb24b7e96f7";

        // when
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(password, salt);

        // then
        assertThat(encryptedPassword).isEqualTo(result);
    }
}
