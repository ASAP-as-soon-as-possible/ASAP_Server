package com.asap.server.service;

import com.asap.server.config.jwt.JwtService;
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
    private final JwtService jwtService;
    @Transactional
    public String createHost(String name){
        User newUser = User.newInstance(name, Role.HOST);
        userRepository.save(newUser);
        return jwtService.issuedToken(newUser.getId().toString());
    }
}
