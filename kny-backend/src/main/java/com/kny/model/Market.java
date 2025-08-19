package com.kny.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "markets")
public class Market {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "google_place_id", unique = true, length = 255)
    private String googlePlaceId;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;
    
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "opening_time")
    private LocalTime openingTime;
    
    @Column(name = "closing_time")
    private LocalTime closingTime;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String specialties;
    
    @Column(columnDefinition = "TEXT")
    private String highlights;
    
    @Column(name = "is_covered")
    private Boolean isCovered = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "crowd_level_morning")
    private CrowdLevel crowdLevelMorning = CrowdLevel.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "crowd_level_afternoon")
    private CrowdLevel crowdLevelAfternoon = CrowdLevel.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "crowd_level_evening")
    private CrowdLevel crowdLevelEvening = CrowdLevel.LOW;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_source")
    private DataSource dataSource = DataSource.HYBRID;
    
    @Column(name = "last_google_sync")
    private LocalDateTime lastGoogleSync;
    
    // REMOVED: google_rating and google_total_ratings fields
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "website", length = 500)
    private String website;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Enums
    public enum CrowdLevel {
        LOW, MEDIUM, HIGH
    }
    
    public enum DataSource {
        LOCAL, GOOGLE, HYBRID
    }
    
    // Default constructor
    public Market() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor
    public Market(String name, String address, BigDecimal latitude, BigDecimal longitude) {
        this();
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters (removed rating-related methods)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getGooglePlaceId() { return googlePlaceId; }
    public void setGooglePlaceId(String googlePlaceId) { this.googlePlaceId = googlePlaceId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public LocalTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }
    
    public LocalTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSpecialties() { return specialties; }
    public void setSpecialties(String specialties) { this.specialties = specialties; }
    
    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }
    
    public Boolean getIsCovered() { return isCovered; }
    public void setIsCovered(Boolean isCovered) { this.isCovered = isCovered; }
    
    public CrowdLevel getCrowdLevelMorning() { return crowdLevelMorning; }
    public void setCrowdLevelMorning(CrowdLevel crowdLevelMorning) { this.crowdLevelMorning = crowdLevelMorning; }
    
    public CrowdLevel getCrowdLevelAfternoon() { return crowdLevelAfternoon; }
    public void setCrowdLevelAfternoon(CrowdLevel crowdLevelAfternoon) { this.crowdLevelAfternoon = crowdLevelAfternoon; }
    
    public CrowdLevel getCrowdLevelEvening() { return crowdLevelEvening; }
    public void setCrowdLevelEvening(CrowdLevel crowdLevelEvening) { this.crowdLevelEvening = crowdLevelEvening; }
    
    public DataSource getDataSource() { return dataSource; }
    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
    
    public LocalDateTime getLastGoogleSync() { return lastGoogleSync; }
    public void setLastGoogleSync(LocalDateTime lastGoogleSync) { this.lastGoogleSync = lastGoogleSync; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}