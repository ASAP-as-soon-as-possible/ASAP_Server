package com.asap.server.service.vo;

import com.querydsl.core.annotations.QueryProjection;

public record UserVo(
        Long id,
        String name
) {
    @QueryProjection
    public UserVo {
    }
}
