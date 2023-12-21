package com.asap.server.repository.timeblock;

import com.asap.server.service.vo.TimeBlockVo;

import java.util.List;

public interface TimeBlockRepositoryCustom {
    List<TimeBlockVo> findByAvailableDate(final Long availableId);
}
