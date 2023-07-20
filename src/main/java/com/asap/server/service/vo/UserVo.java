package com.asap.server.service.vo;

import com.asap.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserVo {
    private String name;

    public static UserVo of(User user) {
        return new UserVo(user.getName());
    }
}
