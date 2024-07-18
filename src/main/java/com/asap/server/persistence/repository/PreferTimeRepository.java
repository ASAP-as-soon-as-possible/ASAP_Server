package com.asap.server.persistence.repository;

import com.asap.server.persistence.domain.Meeting;
import com.asap.server.persistence.domain.PreferTime;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PreferTimeRepository extends Repository<PreferTime, Long> {

    PreferTime save(final PreferTime preferTime);

    List<PreferTime> findByMeeting(final Meeting meeting);
}
