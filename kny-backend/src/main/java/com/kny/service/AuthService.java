package com.kny.service;

import com.kny.dto.LoginRequest;
import com.kny.dto.RegisterRequest;
import com.kny.dto.UserResponse;
import com.kny.model.User;
import com.kny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Register a new user
     */
    public UserResponse registerUser(RegisterRequest registerRequest) {
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new RuntimeException("Email already exists: " + registerRequest.getEmail());
            }

            // Create new user
            User newUser = new User();
            newUser.setName(registerRequest.getName());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setProvider("email");
            newUser.setIsActive(true);

            // Save user to database
            User savedUser = userRepository.save(newUser);

            // Return user response (without password)
            return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                null // No token for registration, only for login
            );

        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    /**
     * Authenticate user login
     */
    public UserResponse authenticate(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Get user details
            User user = userRepository.findActiveUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Return user response
            return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null // You can generate JWT token here if needed
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Register user with just name, email, password (alternative method)
     */
    public UserResponse registerUser(String name, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest(name, email, password);
        return registerUser(registerRequest);
    }

    /**
     * Find user by email
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}