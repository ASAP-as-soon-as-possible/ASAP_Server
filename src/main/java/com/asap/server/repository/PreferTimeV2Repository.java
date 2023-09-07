package com.asap.server.repository;

import com.asap.server.domain.MeetingV2;
import com.asap.server.domain.PreferTimeV2;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.List;

public interface PreferTimeV2Repository extends Repository<PreferTimeV2, Long> {

    PreferTimeV2 save(PreferTimeV2 preferTimeV2);

    List<PreferTimeV2> findByMeeting(MeetingV2 meetingV2);
}
