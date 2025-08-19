package com.kny.dto;

import com.kny.model.Market;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class MarketResponse {
    private Long id;
    private String googlePlaceId;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String description;
    private String specialties;
    private String highlights;
    private Boolean isCovered;
    private Market.CrowdLevel crowdLevelMorning;
    private Market.CrowdLevel crowdLevelAfternoon;
    private Market.CrowdLevel crowdLevelEvening;
    private Market.DataSource dataSource;
    private String phoneNumber;
    private String website;
    private List<MarketPhotoResponse> photos;
    private Boolean isOpen;
    private String currentCrowdLevel;
    
    public MarketResponse() {}
    
    public MarketResponse(Market market) {
        this.id = market.getId();
        this.googlePlaceId = market.getGooglePlaceId();
        this.name = market.getName();
        this.address = market.getAddress();
        this.latitude = market.getLatitude();
        this.longitude = market.getLongitude();
        this.openingTime = market.getOpeningTime();
        this.closingTime = market.getClosingTime();
        this.description = market.getDescription();
        this.specialties = market.getSpecialties();
        this.highlights = market.getHighlights();
        this.isCovered = market.getIsCovered();
        this.crowdLevelMorning = market.getCrowdLevelMorning();
        this.crowdLevelAfternoon = market.getCrowdLevelAfternoon();
        this.crowdLevelEvening = market.getCrowdLevelEvening();
        this.dataSource = market.getDataSource();
        this.phoneNumber = market.getPhoneNumber();
        this.website = market.getWebsite();
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
    
    public Market.CrowdLevel getCrowdLevelMorning() { return crowdLevelMorning; }
    public void setCrowdLevelMorning(Market.CrowdLevel crowdLevelMorning) { this.crowdLevelMorning = crowdLevelMorning; }
    
    public Market.CrowdLevel getCrowdLevelAfternoon() { return crowdLevelAfternoon; }
    public void setCrowdLevelAfternoon(Market.CrowdLevel crowdLevelAfternoon) { this.crowdLevelAfternoon = crowdLevelAfternoon; }
    
    public Market.CrowdLevel getCrowdLevelEvening() { return crowdLevelEvening; }
    public void setCrowdLevelEvening(Market.CrowdLevel crowdLevelEvening) { this.crowdLevelEvening = crowdLevelEvening; }
    
    public Market.DataSource getDataSource() { return dataSource; }
    public void setDataSource(Market.DataSource dataSource) { this.dataSource = dataSource; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public List<MarketPhotoResponse> getPhotos() { return photos; }
    public void setPhotos(List<MarketPhotoResponse> photos) { this.photos = photos; }
    
    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }
    
    public String getCurrentCrowdLevel() { return currentCrowdLevel; }
    public void setCurrentCrowdLevel(String currentCrowdLevel) { this.currentCrowdLevel = currentCrowdLevel; }
}