package com.kny.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesService {

    @Value("${app.google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
    private static final String PLACES_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo";

    public GooglePlacesService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // Get place photos only
    public List<String> getPlacePhotos(String placeId, int maxPhotos) {
        List<String> photos = new ArrayList<>();
        
        if (placeId == null || apiKey == null) {
            return photos;
        }

        try {
            String url = String.format(
                "%s?place_id=%s&fields=photos&key=%s",
                PLACES_DETAILS_URL, placeId, apiKey
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            if (root.has("result") && root.get("result").has("photos")) {
                JsonNode photosNode = root.get("result").get("photos");
                
                int count = 0;
                for (JsonNode photo : photosNode) {
                    if (count >= maxPhotos) break;
                    
                    if (photo.has("photo_reference")) {
                        String photoRef = photo.get("photo_reference").asText();
                        String photoUrl = String.format(
                            "%s?maxwidth=400&photo_reference=%s&key=%s",
                            PLACES_PHOTO_URL, photoRef, apiKey
                        );
                        photos.add(photoUrl);
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting photos: " + e.getMessage());
        }
        
        return photos;
    }

    // Get basic place details
    public PlaceDetails getPlaceDetails(String placeId) {
        if (placeId == null || placeId.trim().isEmpty()) {
            return null;
        }

        try {
            String url = String.format(
                "%s?place_id=%s&fields=name,formatted_phone_number,website&key=%s",
                PLACES_DETAILS_URL, placeId, apiKey
            );

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            if (root.has("result")) {
                JsonNode result = root.get("result");
                PlaceDetails details = new PlaceDetails();
                
                details.setPlaceId(placeId);
                details.setName(getString(result, "name"));
                details.setPhoneNumber(getString(result, "formatted_phone_number"));
                details.setWebsite(getString(result, "website"));
              
                
                return details;
            }
        } catch (Exception e) {
            System.err.println("Error getting place details: " + e.getMessage());
        }
        
        return null;
    }

    // Update market with Google data 
    public void updateMarketWithGoogle(com.kny.model.Market market) {
        if (market.getGooglePlaceId() == null) {
            return;
        }

        try {
            PlaceDetails details = getPlaceDetails(market.getGooglePlaceId());
            
            if (details != null) {
                
                // Update basic info only
                if (details.getPhoneNumber() != null) {
                    market.setPhoneNumber(details.getPhoneNumber());
                }
                
                if (details.getWebsite() != null) {
                    market.setWebsite(details.getWebsite());
                }
                
                market.setLastGoogleSync(java.time.LocalDateTime.now());
                System.out.println("Updated market: " + market.getName());
            }
        } catch (Exception e) {
            System.err.println("Error updating market: " + e.getMessage());
        }
    }

    // Helper method
    private String getString(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }

    // Simplified PlaceDetails class 
    public static class PlaceDetails {
        private String placeId;
        private String name;
        private String phoneNumber;
        private String website;

        // Getters and Setters
        public String getPlaceId() { return placeId; }
        public void setPlaceId(String placeId) { this.placeId = placeId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
    }
}
