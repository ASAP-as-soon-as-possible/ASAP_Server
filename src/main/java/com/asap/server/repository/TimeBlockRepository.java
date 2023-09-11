package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import com.asap.server.domain.TimeBlock;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TimeBlockRepository extends Repository<TimeBlock, Long> {
    List<TimeBlock> findByAvailableDate(final AvailableDate availableDate);
}
