package com.asap.server.repository.timeblock;

import com.asap.server.repository.timeblock.dto.TimeBlockDto;
import java.util.List;

public interface TimeBlockRepositoryCustom {
    List<TimeBlockDto> findAllTimeBlockByMeeting(final Long meetingId);
}
