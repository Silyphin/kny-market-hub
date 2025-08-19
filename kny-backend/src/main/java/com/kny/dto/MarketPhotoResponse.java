package com.kny.dto;

// Simple DTO for photo data from Google Places API
public class MarketPhotoResponse {
    private Long id; // Just for frontend identification
    private String photoUrl; // Google Places photo URL
    private Boolean isPrimary; // First photo is primary
    private String source; // Always "GOOGLE" for Google Places photos
    
    // Default constructor
    public MarketPhotoResponse() {
        this.source = "GOOGLE";
    }
    
    // Constructor with URL
    public MarketPhotoResponse(String photoUrl, Boolean isPrimary) {
        this();
        this.photoUrl = photoUrl;
        this.isPrimary = isPrimary;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}