package com.asap.server.repository;

import com.asap.server.domain.PreferTimeV2;
import org.springframework.data.repository.Repository;

public interface PreferTimeV2Repository extends Repository<PreferTimeV2, Long> {

    PreferTimeV2 save(PreferTimeV2 preferTimeV2);
}
