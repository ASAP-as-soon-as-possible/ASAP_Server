package com.asap.server.repository;

import com.asap.server.domain.AvailableDate;
import org.springframework.data.repository.Repository;

public interface AvailableDateRepository extends Repository<AvailableDate, Long> {
    AvailableDate save(AvailableDate availableDate);
}
