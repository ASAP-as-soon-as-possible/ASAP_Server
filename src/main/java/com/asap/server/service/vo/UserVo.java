package com.asap.server.service.vo;

import com.asap.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String name;

    public static UserVo of(final User user) {
        return new UserVo(user.getId(), user.getName());
    }
}
