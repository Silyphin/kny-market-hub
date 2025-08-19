package com.kny.controller;

import com.kny.dto.LoginRequest;
import com.kny.dto.RegisterRequest;
import com.kny.dto.UserResponse;
import com.kny.service.AuthService;
import com.kny.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        // Create MockMvc without Spring context
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        
        loginRequest = new LoginRequest("test@example.com", "password123");
        registerRequest = new RegisterRequest("John Doe", "john@example.com", "password123");
        userResponse = new UserResponse(1L, "John Doe", "john@example.com", null);
    }

    @Test
    @DisplayName("Should login successfully")
    void testLoginSuccess() throws Exception {
        // Given
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.message").value("Login successful"));

        verify(authService).authenticate(any(LoginRequest.class));
    }

    @Test
    @DisplayName("Should handle login failure")
    void testLoginFailure() throws Exception {
        // Given
        when(authService.authenticate(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Login failed: Invalid email or password"));

        verify(authService).authenticate(any(LoginRequest.class));
    }

    @Test
    @DisplayName("Should register successfully")
    void testRegisterSuccess() throws Exception {
        // Given
        when(userService.existsByEmail("john@example.com")).thenReturn(false);
        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.message").value("Registration successful"));

        verify(userService).existsByEmail("john@example.com");
        verify(authService).registerUser(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("Should handle registration with existing email")
    void testRegisterWithExistingEmail() throws Exception {
        // Given
        when(userService.existsByEmail("john@example.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already exists"));

        verify(userService).existsByEmail("john@example.com");
        verify(authService, never()).registerUser(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("Should handle login with missing email")
    void testLoginWithMissingEmail() throws Exception {
        // Given
        LoginRequest invalidRequest = new LoginRequest(null, "password123");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email and password are required"));

        verify(authService, never()).authenticate(any(LoginRequest.class));
    }
}