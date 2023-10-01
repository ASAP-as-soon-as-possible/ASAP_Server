package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreferTimeRepository extends Repository<PreferTime, Long> {

    PreferTime save(final PreferTime preferTime);

    List<PreferTime> findByMeeting(final Meeting meeting);


    @Modifying
    @Query("delete from PreferTime p where p.meeting = :meeting")
    void deleteByMeeting(@Param("meeting") final Meeting meeting);
}
