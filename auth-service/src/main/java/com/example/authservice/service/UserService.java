package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found: " + email)
        );
    }
}
