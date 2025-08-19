package com.kny.repository;

import com.kny.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find user by OAuth provider and OAuth ID
    Optional<User> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find active users
    List<User> findByIsActiveTrue();
    
    // Find users by OAuth provider
    List<User> findByOauthProvider(String oauthProvider);
    
    // Find users by name containing keyword (case insensitive)
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Custom query to find user by email and active status
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);
    
    // Count active users
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
}