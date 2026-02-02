package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.dto.UserResponse;
import com.example.authservice.entity.User;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.JwtService;
import com.example.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody LoginRequest request){
        log.info("Login attempt: {}, {}", request.email(), request.password());
        authService.signIn(request);
        User user = userService.getUserByEmail(request.email());
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken)
                .body(new UserResponse(user.getEmail()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestParam RegisterRequest request){
        User user = userService.createUser(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.getEmail()));
    }


}
