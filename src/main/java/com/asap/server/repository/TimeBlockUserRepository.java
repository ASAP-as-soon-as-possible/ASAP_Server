package com.asap.server.repository;

import com.asap.server.domain.TimeBlock;
import com.asap.server.domain.TimeBlockUser;
import com.asap.server.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeBlockUserRepository extends Repository<TimeBlockUser, Long> {

    void save(final TimeBlockUser timeBlockUser);

    List<TimeBlockUser> findAllByUser(final User user);

    List<TimeBlockUser> findByTimeBlock(final TimeBlock timeBlock);

    @Modifying
    @Query("delete from TimeBlockUser t where t.timeBlock in :timeblocks")
    void deleteByTimeBlocksIn(@Param("timeblocks") final List<TimeBlock> timeBlocks);
}
