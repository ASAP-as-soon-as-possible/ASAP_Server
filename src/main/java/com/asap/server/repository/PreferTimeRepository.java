package com.asap.server.repository;

import com.asap.server.domain.Meeting;
import com.asap.server.domain.PreferTime;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PreferTimeRepository extends Repository<PreferTime, Long> {

    PreferTime save(final PreferTime preferTime);

    List<PreferTime> findByMeeting(final Meeting meeting);
}
