package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.event.UserRegisterEvent;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found: " + email)
        );
    }

    public User createUser(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException("User already exists: " + email);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user =  userRepository.save(user);
        kafkaTemplate.send("user-events", new UserRegisterEvent(user.getEmail()));
        return user;
    }
}
