package com.asap.server.persistence.repository.timeblock;

import com.asap.server.persistence.repository.timeblock.dto.TimeBlockDto;
import java.util.List;

public interface TimeBlockRepositoryCustom {
    List<TimeBlockDto> findAllTimeBlockByMeeting(final Long meetingId);
}
