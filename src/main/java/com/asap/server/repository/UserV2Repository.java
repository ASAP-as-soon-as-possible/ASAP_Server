package com.asap.server.repository;

import com.asap.server.domain.UserV2;
import org.springframework.data.repository.Repository;

public interface UserV2Repository extends Repository<UserV2, Long> {
    UserV2 save(UserV2 userV2);
}
