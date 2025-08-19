package com.kny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String name;
    
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false, length = 255)
    private String email;
    
    // FIXED: Use only ONE password field that matches your database
    @Column(name = "password", length = 255)
    private String password;
    
    @Column(name = "provider", length = 20)
    private String provider = "email";
    
    @Column(name = "provider_id", length = 255)
    private String providerId;
    
    @Column(name = "profile_picture", length = 255)
    private String profilePicture;
    
    @Column(name = "phone_number", length = 255)
    private String phoneNumber;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "oauth_id", length = 255)
    private String oauthId;
    
    @Column(name = "oauth_provider", length = 255)
    private String oauthProvider;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getProvider() { 
        return provider; 
    }
    
    public void setProvider(String provider) { 
        this.provider = provider; 
    }
    
    public String getProviderId() { 
        return providerId; 
    }
    
    public void setProviderId(String providerId) { 
        this.providerId = providerId; 
    }
    
    public String getProfilePicture() { 
        return profilePicture; 
    }
    
    public void setProfilePicture(String profilePicture) { 
        this.profilePicture = profilePicture; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    public Boolean getIsActive() { 
        return isActive; 
    }
    
    public void setIsActive(Boolean isActive) { 
        this.isActive = isActive; 
    }
    
    public String getOauthId() { 
        return oauthId; 
    }
    
    public void setOauthId(String oauthId) { 
        this.oauthId = oauthId; 
    }
    
    public String getOauthProvider() { 
        return oauthProvider; 
    }
    
    public void setOauthProvider(String oauthProvider) { 
        this.oauthProvider = oauthProvider; 
    }
    
    // REMOVED: passwordHash methods since we're using password field only
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
    
    // Utility methods for provider validation
    public boolean isEmailProvider() {
        return "email".equals(this.provider);
    }
    
    public boolean isGoogleProvider() {
        return "google".equals(this.provider);
    }
    
    public boolean isFacebookProvider() {
        return "facebook".equals(this.provider);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", provider='" + provider + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}