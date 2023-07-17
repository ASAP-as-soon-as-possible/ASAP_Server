package com.asap.server.repository;

import com.asap.server.domain.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    void save(User user);
    Optional<User> findById(Long id);

}
