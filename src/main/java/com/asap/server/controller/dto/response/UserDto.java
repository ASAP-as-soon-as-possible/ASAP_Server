package com.asap.server.controller.dto.response;

import com.asap.server.domain.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;

    public static UserDto of(User user) {
        return new UserDto(user.getId(), user.getName());
    }
}
