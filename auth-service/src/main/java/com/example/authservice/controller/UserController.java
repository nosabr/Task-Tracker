package com.example.authservice.controller;

import com.example.authservice.dto.UserResponse;
import com.example.authservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(new UserResponse(user.getEmail()));
    }
}
