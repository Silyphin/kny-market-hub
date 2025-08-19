package com.kny.controller;

import com.kny.dto.MarketResponse;
import com.kny.service.MarketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarketController Unit Tests")
class MarketControllerTest {

    @Mock
    private MarketService marketService;

    @InjectMocks
    private MarketController marketController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MarketResponse marketResponse1;
    private MarketResponse marketResponse2;
    private List<MarketResponse> marketsList;

    @BeforeEach
    void setUp() {
        // Create MockMvc without Spring context
        mockMvc = MockMvcBuilders.standaloneSetup(marketController).build();
        objectMapper = new ObjectMapper();
        
        marketResponse1 = createMarketResponse(1L, "Test Market 1", "Address 1");
        marketResponse2 = createMarketResponse(2L, "Test Market 2", "Address 2");
        marketsList = Arrays.asList(marketResponse1, marketResponse2);
    }

    @Test
    @DisplayName("Should get all markets successfully")
    void testGetAllMarkets() throws Exception {
        // Given
        when(marketService.getAllMarkets()).thenReturn(marketsList);

        // When & Then
        mockMvc.perform(get("/api/markets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Market 1"))
                .andExpect(jsonPath("$[1].name").value("Test Market 2"));

        verify(marketService).getAllMarkets();
    }

    @Test
    @DisplayName("Should get market by ID when found")
    void testGetMarketById_Found() throws Exception {
        // Given
        when(marketService.getMarketById(1L)).thenReturn(Optional.of(marketResponse1));

        // When & Then
        mockMvc.perform(get("/api/markets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Market 1"))
                .andExpect(jsonPath("$.address").value("Address 1"));

        verify(marketService).getMarketById(1L);
    }

    @Test
    @DisplayName("Should return 404 when market not found")
    void testGetMarketById_NotFound() throws Exception {
        // Given
        when(marketService.getMarketById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/markets/999"))
                .andExpect(status().isNotFound());

        verify(marketService).getMarketById(999L);
    }

    @Test
    @DisplayName("Should search markets by name")
    void testSearchMarketsByName() throws Exception {
        // Given
        when(marketService.getMarketsByName("Test")).thenReturn(marketsList);

        // When & Then
        mockMvc.perform(get("/api/markets/search")
                .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(marketService).getMarketsByName("Test");
        verify(marketService, never()).getMarketsBySpecialty(any());
        verify(marketService, never()).getAllMarkets();
    }

    @Test
    @DisplayName("Should get nearby markets")
    void testGetNearbyMarkets() throws Exception {
        // Given
        when(marketService.getMarketsWithinRadius(
                any(BigDecimal.class), any(BigDecimal.class), eq(10.0)))
                .thenReturn(marketsList);

        // When & Then
        mockMvc.perform(get("/api/markets/nearby")
                .param("latitude", "5.4164")
                .param("longitude", "100.3327")
                .param("radius", "10.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(marketService).getMarketsWithinRadius(
                any(BigDecimal.class), any(BigDecimal.class), eq(10.0));
    }

    @Test
    @DisplayName("Should get market statistics")
    void testGetMarketStatistics() throws Exception {
        // Given
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMarkets", 15);
        stats.put("coveredMarkets", 8);
        when(marketService.getMarketStatistics()).thenReturn(stats);

        // When & Then
        mockMvc.perform(get("/api/markets/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMarkets").value(15))
                .andExpect(jsonPath("$.coveredMarkets").value(8));

        verify(marketService).getMarketStatistics();
    }

    @Test
    @DisplayName("Should sync market successfully")
    void testSyncMarketSuccess() throws Exception {
        // Given
        doNothing().when(marketService).syncWithGooglePlaces(1L);

        // When & Then
        mockMvc.perform(post("/api/markets/1/sync"))
                .andExpect(status().isOk())
                .andExpect(content().string("Market synced successfully"));
        verify(marketService).syncWithGooglePlaces(1L);
    }

    @Test
    @DisplayName("Should handle sync market failure")
    void testSyncMarketFailure() throws Exception {
        // Given
        doThrow(new RuntimeException("Google API error"))
                .when(marketService).syncWithGooglePlaces(1L);

        // When & Then
        mockMvc.perform(post("/api/markets/1/sync"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Sync failed: Google API error")); 

        verify(marketService).syncWithGooglePlaces(1L);
    }

    private MarketResponse createMarketResponse(Long id, String name, String address) {
        MarketResponse response = new MarketResponse();
        response.setId(id);
        response.setName(name);
        response.setAddress(address);
        response.setLatitude(new BigDecimal("5.4164"));
        response.setLongitude(new BigDecimal("100.3327"));
        return response;
    }
}