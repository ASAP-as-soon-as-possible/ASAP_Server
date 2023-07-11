package com.asap.server.repository;

import com.asap.server.domain.DateAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateAvailabilityRepository extends JpaRepository<DateAvailability, Long> {
}
