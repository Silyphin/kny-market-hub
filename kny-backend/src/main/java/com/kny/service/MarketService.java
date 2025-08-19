package com.kny.service;

import com.kny.dto.MarketResponse;
import com.kny.dto.MarketPhotoResponse;
import com.kny.model.Market;
import com.kny.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class MarketService {
    
    @Autowired
    private MarketRepository marketRepository;
    
    @Autowired
    private GooglePlacesService googlePlacesService;
    
    // Get all markets 
    public List<MarketResponse> getAllMarkets() {
        List<Market> markets = marketRepository.findAllByOrderByNameAsc();
        return markets.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Convert market to response
    private MarketResponse convertToResponse(Market market) {
        MarketResponse response = new MarketResponse(market);
        
        // Set basic calculated fields
        response.setIsOpen(isMarketOpen(market));
        response.setCurrentCrowdLevel(getCurrentCrowdLevel(market));
        
        // Get photos
        List<MarketPhotoResponse> photos = getMarketPhotos(market.getGooglePlaceId());
        response.setPhotos(photos);
        
        return response;
    }
    
    // Get market by ID 
    public Optional<MarketResponse> getMarketById(Long id) {
        return marketRepository.findById(id)
                .map(this::convertToResponse);
    }
    

    public List<MarketResponse> getMarketsByName(String name) {
        List<Market> markets = marketRepository.findByNameContainingIgnoreCase(name);
        return markets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public List<MarketResponse> getMarketsWithinRadius(BigDecimal latitude, BigDecimal longitude, double radiusKm) {
        List<Market> markets = marketRepository.findMarketsWithinRadius(latitude, longitude, radiusKm);
        return markets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public List<MarketResponse> getMarketsBySpecialty(String specialty) {
        List<Market> markets = marketRepository.findBySpecialtiesContaining(specialty);
        return markets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public List<MarketResponse> getCoveredMarkets() {
        List<Market> markets = marketRepository.findByIsCoveredTrue();
        return markets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    public List<MarketResponse> getMarketsByCrowdLevel(String timeOfDay, Market.CrowdLevel crowdLevel) {
        List<Market> markets;
        switch (timeOfDay.toLowerCase()) {
            case "morning":
                markets = marketRepository.findByCrowdLevelMorning(crowdLevel);
                break;
            case "afternoon":
                markets = marketRepository.findByCrowdLevelAfternoon(crowdLevel);
                break;
            case "evening":
                markets = marketRepository.findByCrowdLevelEvening(crowdLevel);
                break;
            default:
                markets = marketRepository.findByCrowdLevelMorning(crowdLevel);
        }
        return markets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    

    
    // Get market photos
    private List<MarketPhotoResponse> getMarketPhotos(String placeId) {
        List<MarketPhotoResponse> photoResponses = new ArrayList<>();
        
        if (placeId == null || placeId.trim().isEmpty()) {
            return photoResponses;
        }
        
        try {
            List<String> photoUrls = googlePlacesService.getPlacePhotos(placeId, 3);
            
            for (int i = 0; i < photoUrls.size(); i++) {
                MarketPhotoResponse photo = new MarketPhotoResponse();
                photo.setId((long) i);
                photo.setPhotoUrl(photoUrls.get(i));
                photo.setIsPrimary(i == 0);
                photoResponses.add(photo);
            }
        } catch (Exception e) {
            System.err.println("Error loading photos: " + e.getMessage());
        }
        
        return photoResponses;
    }
    
    //Check if market is open
    private Boolean isMarketOpen(Market market) {
        if (market.getOpeningTime() == null || market.getClosingTime() == null) {
            return null;
        }
        
        LocalTime now = LocalTime.now();
        LocalTime opening = market.getOpeningTime();
        LocalTime closing = market.getClosingTime();
        
        if (closing.isBefore(opening)) {
            // Market is open overnight
            return now.isAfter(opening) || now.isBefore(closing);
        } else {
            // Regular hours
            return now.isAfter(opening) && now.isBefore(closing);
        }
    }
    
    //Get current crowd level
    private String getCurrentCrowdLevel(Market market) {
        LocalTime now = LocalTime.now();
        
        if (now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(12, 0))) {
            return market.getCrowdLevelMorning() != null ? 
                   market.getCrowdLevelMorning().toString().toLowerCase() : "medium";
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(18, 0))) {
            return market.getCrowdLevelAfternoon() != null ? 
                   market.getCrowdLevelAfternoon().toString().toLowerCase() : "medium";
        } else {
            return market.getCrowdLevelEvening() != null ? 
                   market.getCrowdLevelEvening().toString().toLowerCase() : "low";
        }
    }
    
    //Sync with Google Places
    public void syncWithGooglePlaces(Long marketId) {
        try {
            Optional<Market> marketOpt = marketRepository.findById(marketId);
            if (marketOpt.isPresent()) {
                Market market = marketOpt.get();
                googlePlacesService.updateMarketWithGoogle(market);
                marketRepository.save(market);
                System.out.println("Synced market: " + market.getName());
            }
        } catch (Exception e) {
            System.err.println("Sync failed: " + e.getMessage());
        }
    }
    
    //Sync all markets
    public void syncAllMarkets() {
        List<Market> markets = marketRepository.findAll();
        
        for (Market market : markets) {
            if (market.getGooglePlaceId() != null) {
                try {
                    googlePlacesService.updateMarketWithGoogle(market);
                    marketRepository.save(market);
                    System.out.println("Synced: " + market.getName());
                    
                    // Small delay to avoid rate limits
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.err.println("Error syncing " + market.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    // Get market statistics
    public java.util.Map<String, Object> getMarketStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalMarkets", marketRepository.count());
        stats.put("coveredMarkets", marketRepository.findByIsCoveredTrue().size());
        return stats;
    }
    
    // Get markets that need sync
    public List<Market> getMarketsNeedingSync(int daysOld) {
        java.time.LocalDateTime cutoffDate = java.time.LocalDateTime.now().minusDays(daysOld);
        return marketRepository.findMarketsNeedingGoogleSync(cutoffDate);
    }
}