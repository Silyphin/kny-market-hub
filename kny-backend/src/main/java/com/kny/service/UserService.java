package com.kny.service;

import com.kny.model.User;
import com.kny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return userOpt.get();
            } else {
                throw new RuntimeException("User not found with email: " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user: " + e.getMessage());
        }
    }

    public User findById(Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                return userOpt.get();
            } else {
                throw new RuntimeException("User not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user: " + e.getMessage());
        }
    }

    public User updateUser(String email, User updatedUser) {
        try {
            User existingUser = findByEmail(email);
            
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if (updatedUser.getProfilePicture() != null) {
                existingUser.setProfilePicture(updatedUser.getProfilePicture());
            }
            
            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public User createUser(String name, String email, String password) {
        try {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists: " + email);
            }

            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            // FIXED: Use setPassword instead of setPasswordHash
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setProvider("email");
            newUser.setIsActive(true);
            
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        try {
            User user = findById(id);
            user.setIsActive(false);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

    public List<User> getAllActiveUsers() {
        try {
            return userRepository.findByIsActiveTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active users: " + e.getMessage());
        }
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try {
            User user = findByEmail(email);
            
            // FIXED: Use getPassword instead of getPasswordHash
            if (user.getPassword() != null && 
                passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            } else {
                throw new RuntimeException("Old password is incorrect");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage());
        }
    }

    public List<User> searchUsers(String keyword) {
        try {
            return userRepository.findByNameContainingIgnoreCase(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search users: " + e.getMessage());
        }
    }

    public long getActiveUserCount() {
        try {
            return userRepository.countActiveUsers();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user count: " + e.getMessage());
        }
    }

    public User findByOAuth(String provider, String oauthId) {
        try {
            Optional<User> userOpt = userRepository.findByOauthProviderAndOauthId(provider, oauthId);
            return userOpt.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find OAuth user: " + e.getMessage());
        }
    }

    public boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check email existence: " + e.getMessage());
        }
    }
}