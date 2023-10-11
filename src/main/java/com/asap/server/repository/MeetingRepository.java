package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetingRepository extends Repository<Meeting, Long> {
    Optional<Meeting> findById(final Long id);

    Meeting save(final Meeting meeting);

    void saveAndFlush(final Meeting meeting);

    @EntityGraph(attributePaths = {"host"})
    @Query("select m from Meeting m where m.id = :id")
    Optional<Meeting> findByIdWithHost(@Param("id") final Long id);
}
