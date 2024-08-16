package com.asap.server.persistence.repository;

import com.asap.server.persistence.domain.TimeBlock;
import com.asap.server.persistence.domain.TimeBlockUser;
import com.asap.server.persistence.domain.user.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TimeBlockUserRepository extends Repository<TimeBlockUser, Long> {

    void save(final TimeBlockUser timeBlockUser);

    List<TimeBlockUser> findAllByUser(final User user);

    List<TimeBlockUser> findByTimeBlock(final TimeBlock timeBlock);
}
