package com.ty.auth_service.service;

import com.ty.auth_service.model.User;
import com.ty.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findBYEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
