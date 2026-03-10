package com.example.authservice.service;

import com.example.authservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET =
            "dGVzdFNlY3JldEtleUZvckp3dFRva2VuR2VuZXJhdGlvbkFuZFZhbGlkYXRpb24xMjM0NTY3ODk=";

    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", 86_400_000L);

        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .enabled(true)
                .build();
    }

    @Test
    void generateToken_returnsNonBlankToken() {
        assertThat(jwtService.generateToken(user)).isNotBlank();
    }

    @Test
    void extractUserId_returnsCorrectId() {
        String token = jwtService.generateToken(user);
        assertThat(jwtService.extractUserId(token)).isEqualTo(1L);
    }

    @Test
    void extractEmail_returnsCorrectEmail() {
        String token = jwtService.generateToken(user);
        assertThat(jwtService.extractEmail(token)).isEqualTo("user@example.com");
    }

    @Test
    void isTokenValid_freshTokenIsValid() {
        String token = jwtService.generateToken(user);
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_expiredTokenIsInvalid() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1L);
        String token = jwtService.generateToken(user);
        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void isTokenValid_garbageStringIsInvalid() {
        assertThat(jwtService.isTokenValid("not.a.jwt")).isFalse();
    }
}