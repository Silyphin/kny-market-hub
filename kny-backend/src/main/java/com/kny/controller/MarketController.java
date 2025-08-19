package com.kny.controller;

import com.kny.dto.MarketResponse;
import com.kny.model.Market;
import com.kny.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/markets")
public class MarketController {
    
    @Autowired
    private MarketService marketService;
    
    @GetMapping
    public ResponseEntity<List<MarketResponse>> getAllMarkets() {
        List<MarketResponse> markets = marketService.getAllMarkets();
        return ResponseEntity.ok(markets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MarketResponse> getMarketById(@PathVariable Long id) {
        Optional<MarketResponse> market = marketService.getMarketById(id);
        return market.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MarketResponse>> searchMarkets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty) {
        
        List<MarketResponse> markets;
        if (name != null && !name.trim().isEmpty()) {
            markets = marketService.getMarketsByName(name);
        } else if (specialty != null && !specialty.trim().isEmpty()) {
            markets = marketService.getMarketsBySpecialty(specialty);
        } else {
            markets = marketService.getAllMarkets();
        }
        return ResponseEntity.ok(markets);
    }
    
    @GetMapping("/nearby")
    public ResponseEntity<List<MarketResponse>> getNearbyMarkets(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "10.0") double radius) {
        
        List<MarketResponse> markets = marketService.getMarketsWithinRadius(latitude, longitude, radius);
        return ResponseEntity.ok(markets);
    }
    
    @GetMapping("/covered")
    public ResponseEntity<List<MarketResponse>> getCoveredMarkets() {
        List<MarketResponse> markets = marketService.getCoveredMarkets();
        return ResponseEntity.ok(markets);
    }
    
    @GetMapping("/crowd-level")
    public ResponseEntity<List<MarketResponse>> getMarketsByCrowdLevel(
            @RequestParam String timeOfDay,
            @RequestParam String crowdLevel) {
        
        try {
            Market.CrowdLevel level = Market.CrowdLevel.valueOf(crowdLevel.toUpperCase());
            List<MarketResponse> markets = marketService.getMarketsByCrowdLevel(timeOfDay, level);
            return ResponseEntity.ok(markets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMarketStatistics() {
        Map<String, Object> stats = marketService.getMarketStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/{id}/sync")
    public ResponseEntity<String> syncMarket(@PathVariable Long id) {
        try {
            marketService.syncWithGooglePlaces(id);
            return ResponseEntity.ok("Market synced successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Sync failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new java.util.HashMap<>();
        response.put("status", "healthy");
        response.put("service", "market-service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}