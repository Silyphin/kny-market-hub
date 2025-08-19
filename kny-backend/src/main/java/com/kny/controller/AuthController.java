package com.kny.controller;

import com.kny.dto.LoginRequest;
import com.kny.dto.RegisterRequest;
import com.kny.dto.UserResponse;
import com.kny.model.User;
import com.kny.service.AuthService;
import com.kny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // Registration endpoint
    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if email already exists
            if (userService.existsByEmail(registerRequest.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }

            // Register the user
            UserResponse userResponse = authService.registerUser(registerRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", userResponse);
            response.put("message", "Registration successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // FIXED: API endpoint for custom login that properly persists authentication
    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email and password are required");
                return ResponseEntity.badRequest().body(error);
            }
            
            // FIXED: Authenticate and persist the authentication
            UserResponse userResponse = authService.authenticate(loginRequest);
            
            // CRITICAL: Create authentication token and save to security context
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    null, 
                    java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
                );
            
            // Set the authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            // CRITICAL: Save authentication to session
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            
            System.out.println("Login successful - User: " + userResponse.getEmail()); // DEBUG
            System.out.println("Authentication set in SecurityContext: " + authToken.isAuthenticated()); // DEBUG
            System.out.println("Session ID: " + session.getId()); // DEBUG
            
            // Return consistent response format
            Map<String, Object> response = new HashMap<>();
            response.put("user", userResponse);
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // FIXED: Enhanced getUser method with extensive debugging
    @GetMapping("/api/auth/user")
    @ResponseBody
    public ResponseEntity<?> getUser(Authentication authentication, HttpServletRequest request) {
        try {
            // DEBUG: Log authentication details
            System.out.println("=== AUTH DEBUG ===");
            System.out.println("Authentication object: " + authentication);
            System.out.println("Is authenticated: " + (authentication != null ? authentication.isAuthenticated() : "null"));
            System.out.println("Principal type: " + (authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "null"));
            
            HttpSession session = request.getSession(false);
            System.out.println("Session exists: " + (session != null));
            if (session != null) {
                System.out.println("Session ID: " + session.getId());
                System.out.println("Session user: " + session.getAttribute("user"));
                System.out.println("Session email: " + session.getAttribute("email"));
            }
            
            if (authentication != null && authentication.isAuthenticated()) {
                
                // Check if it's OAuth2 user (Google/Facebook login)
                if (authentication.getPrincipal() instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("name", oauth2User.getAttribute("name"));
                    userInfo.put("email", oauth2User.getAttribute("email"));
                    userInfo.put("picture", oauth2User.getAttribute("picture"));
                    userInfo.put("type", "oauth2");
                    userInfo.put("provider", "google");
                    System.out.println("Returning OAuth2 user: " + userInfo);
                    return ResponseEntity.ok(userInfo);
                }
                
                // Check if it's custom login user (email/password)
                else if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    System.out.println("UserDetails found: " + userDetails.getUsername());
                    
                    try {
                        // Get full user details from database
                        User user = userService.findByEmail(userDetails.getUsername());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", user.getId());
                        userInfo.put("name", user.getName());
                        userInfo.put("email", user.getEmail());
                        userInfo.put("type", "custom");
                        userInfo.put("provider", "email");
                        System.out.println("Returning custom user: " + userInfo);
                        return ResponseEntity.ok(userInfo);
                    } catch (Exception e) {
                        System.out.println("Error getting user from database: " + e.getMessage());
                        // Fallback to basic user info
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("email", userDetails.getUsername());
                        userInfo.put("name", userDetails.getUsername().split("@")[0]);
                        userInfo.put("type", "custom");
                        userInfo.put("provider", "email");
                        return ResponseEntity.ok(userInfo);
                    }
                }
                
                // ENHANCED: Check session for API login (manual session management)
                else {
                    System.out.println("Checking session for manual login...");
                    if (session != null) {
                        UserResponse user = (UserResponse) session.getAttribute("user");
                        String email = (String) session.getAttribute("email");
                        
                        System.out.println("Session user: " + user);
                        System.out.println("Session email: " + email);
                        
                        if (user != null) {
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("id", user.getId());
                            userInfo.put("name", user.getName());
                            userInfo.put("email", user.getEmail());
                            userInfo.put("type", "custom");
                            userInfo.put("provider", "email");
                            System.out.println("Returning session user: " + userInfo);
                            return ResponseEntity.ok(userInfo);
                        }
                        
                        // If we have email in session but no user object, try to get user from DB
                        if (email != null) {
                            try {
                                User dbUser = userService.findByEmail(email);
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("id", dbUser.getId());
                                userInfo.put("name", dbUser.getName());
                                userInfo.put("email", dbUser.getEmail());
                                userInfo.put("type", "custom");
                                userInfo.put("provider", "email");
                                System.out.println("Returning DB user from session email: " + userInfo);
                                return ResponseEntity.ok(userInfo);
                            } catch (Exception e) {
                                System.out.println("Error getting user from DB using session email: " + e.getMessage());
                            }
                        }
                    }
                    
                    // Fallback for other authentication types
                    System.out.println("Using fallback authentication: " + authentication.getName());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("email", authentication.getName());
                    userInfo.put("name", authentication.getName());
                    userInfo.put("type", "other");
                    return ResponseEntity.ok(userInfo);
                }
            }
            
            System.out.println("No valid authentication found");
            Map<String, String> error = new HashMap<>();
            error.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } catch (Exception e) {
            System.out.println("Exception in getUser: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error getting user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Add OAuth2 success handler
    @GetMapping("/oauth2/success")
    public String oauth2Success() {
        return "redirect:http://localhost:3000/dashboard";
    }

    // Add OAuth2 failure handler
    @GetMapping("/oauth2/failure")
    public String oauth2Failure() {
        return "redirect:http://localhost:3000/login?error=oauth2_failed";
    }

    @PostMapping("/api/auth/logout")
    @ResponseBody
    public ResponseEntity<?> apiLogout(HttpServletRequest request) {
        try {
            // Clear session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            // Clear security context
            SecurityContextHolder.clearContext();
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Logout failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Clear session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // Clear security context
        SecurityContextHolder.clearContext();
        
        return "redirect:http://localhost:3000";
    }
}