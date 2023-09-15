package com.asap.server.service.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordInfoVo {
    private String encryptedPassword;
    private String salt;
}
