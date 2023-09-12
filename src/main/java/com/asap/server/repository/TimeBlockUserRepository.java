package com.asap.server.repository;

import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TimeBlockUserRepository extends Repository<TimeBlockUser, Long> {

    void save(final TimeBlockUser timeBlockUser);

    List<TimeBlockUser> findAllByUser(final User user);

    List<TimeBlockUser> findByTimeBlock(final TimeBlock timeBlock);
}
