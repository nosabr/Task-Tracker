package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestParam LoginRequest request){
        log.info("Login attempt: {}, {}", request.getEmail(), request.getPassword());
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(){

    }
}
