package com.asap.server.repository;

import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.UserV2;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TimeBlockUserRepository extends Repository<TimeBlockUser, Long> {

    void save(TimeBlockUser timeBlockUser);
    List<TimeBlockUser> findAllByUser(UserV2 user);
}
