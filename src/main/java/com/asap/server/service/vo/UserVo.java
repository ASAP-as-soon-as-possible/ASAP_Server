package com.asap.server.service.vo;

import com.asap.server.domain.User;
import com.asap.server.domain.UserV2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String name;

    public static UserVo of(User user) {
        return new UserVo(user.getId(), user.getName());
    }
    public static UserVo of(final UserV2 user) {
        return new UserVo(user.getId(), user.getName());
    }
}
