package com.asap.server.repository;

import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.UserV2;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TimeBlockUserRepository extends Repository<TimeBlockUser, Long> {

    void save(final TimeBlockUser timeBlockUser);

    List<TimeBlockUser> findAllByUser(final UserV2 user);

    List<TimeBlockUser> findByTimeBlock(final TimeBlock timeBlock);
}
