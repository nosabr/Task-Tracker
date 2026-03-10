package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock OutboxEventService outboxEventService;
    @Mock KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    UserService userService;

    @Test
    void getUserByEmail_returnsUser_whenExists() {
        User user = buildUser(1L, "alice@example.com");
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        assertThat(userService.getUserByEmail("alice@example.com").getEmail())
                .isEqualTo("alice@example.com");
    }

    @Test
    void getUserByEmail_throwsNotFound_whenMissing() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("ghost@example.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void createUser_savesWithEncodedPassword() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPass")).thenReturn("HASHED");
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return User.builder().id(1L).email(u.getEmail()).password(u.getPassword()).enabled(true).build();
        });

        User result = userService.createUser("bob@example.com", "rawPass");

        assertThat(result.getPassword()).isEqualTo("HASHED");
    }

    @Test
    void createUser_neverStoresPlainTextPassword() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("HASHED");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser("test@example.com", "secret");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).doesNotContain("secret");
    }

    @Test
    void createUser_throwsConflict_whenEmailTaken() {
        when(userRepository.findByEmail("dave@example.com"))
                .thenReturn(Optional.of(buildUser(1L, "dave@example.com")));

        assertThatThrownBy(() -> userService.createUser("dave@example.com", "pass"))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_createsOutboxEvent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("HASHED");
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return User.builder().id(1L).email(u.getEmail()).password(u.getPassword()).enabled(true).build();
        });

        userService.createUser("carol@example.com", "pass");

        verify(outboxEventService, times(1)).createOutboxEvent(any(), any());
    }

    private User buildUser(Long id, String email) {
        return User.builder().id(id).email(email).password("encoded").enabled(true).build();
    }
}