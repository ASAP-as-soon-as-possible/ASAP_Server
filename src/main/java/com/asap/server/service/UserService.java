package com.asap.server.service;

import com.asap.server.domain.User;
import com.asap.server.domain.enums.Role;
import com.asap.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public User createHost(String name){
        User newUser = User.newInstance(name, Role.HOST);
        userRepository.save(newUser);
        return newUser;
    }
}
