package com.kny.service;

import com.kny.dto.LoginRequest;
import com.kny.dto.RegisterRequest;
import com.kny.dto.UserResponse;
import com.kny.model.User;
import com.kny.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setIsActive(true);

        loginRequest = new LoginRequest("john@example.com", "password123");
        registerRequest = new RegisterRequest("John Doe", "john@example.com", "password123");
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void testSuccessfulAuthentication() {
        // Given - Only mock what's actually called in this test
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(userRepository.findActiveUserByEmail("john@example.com"))
            .thenReturn(Optional.of(testUser));

        // When
        UserResponse result = authService.authenticate(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John Doe", result.getName());
        assertEquals(1L, result.getId());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findActiveUserByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testAuthenticationFailure_UserNotFound() {
        // Given - Mock successful authentication but user not found in database
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(userRepository.findActiveUserByEmail("john@example.com"))
            .thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticate(loginRequest);
        });
        
        // Fix: Match the actual error message from your AuthService
        assertTrue(exception.getMessage().contains("User not found"), 
                   "Expected message to contain 'User not found' but was: " + exception.getMessage());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findActiveUserByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should register new user successfully")
    void testSuccessfulRegistration() {
        // Given - Only mock what's actually called in this test
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = authService.registerUser(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John Doe", result.getName());
        
        verify(userRepository).existsByEmail("john@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegistrationWithExistingEmail() {
        // Given - Only mock what's actually called
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });
        
        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository).existsByEmail("john@example.com");
        // Verify save is never called
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should check if email exists")
    void testEmailExists() {
        // Given
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // When
        boolean result = authService.emailExists("john@example.com");

        // Then
        assertTrue(result);
        verify(userRepository).existsByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should check if email does not exist")
    void testEmailDoesNotExist() {
        // Given
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        // When
        boolean result = authService.emailExists("new@example.com");

        // Then
        assertFalse(result);
        verify(userRepository).existsByEmail("new@example.com");
    }
}