package com.kny.service;

import com.kny.dto.MarketResponse;
import com.kny.model.Market;
import com.kny.repository.MarketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarketService Unit Tests")
class MarketServiceTest {

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private GooglePlacesService googlePlacesService;

    @InjectMocks
    private MarketService marketService;

    private Market testMarket1;
    private Market testMarket2;

    @BeforeEach
    void setUp() {
        testMarket1 = new Market();
        testMarket1.setId(1L);
        testMarket1.setName("Test Market 1");
        testMarket1.setAddress("Address 1");
        testMarket1.setLatitude(new BigDecimal("5.4164"));
        testMarket1.setLongitude(new BigDecimal("100.3327"));
        testMarket1.setOpeningTime(LocalTime.of(6, 0));
        testMarket1.setClosingTime(LocalTime.of(18, 0));
        testMarket1.setCrowdLevelMorning(Market.CrowdLevel.HIGH);
        testMarket1.setCrowdLevelAfternoon(Market.CrowdLevel.MEDIUM);
        testMarket1.setCrowdLevelEvening(Market.CrowdLevel.LOW);
        // Set Google Place ID to avoid updateMarketPlaceId calls
        testMarket1.setGooglePlaceId("test-place-id-1");

        testMarket2 = new Market();
        testMarket2.setId(2L);
        testMarket2.setName("Test Market 2");
        testMarket2.setAddress("Address 2");
        testMarket2.setLatitude(new BigDecimal("5.4200"));
        testMarket2.setLongitude(new BigDecimal("100.3400"));
        // Set Google Place ID to avoid updateMarketPlaceId calls
        testMarket2.setGooglePlaceId("test-place-id-2");
    }

    @Test
    @DisplayName("Should get all markets successfully")
    void testGetAllMarkets() {
        // Given
        List<Market> mockMarkets = Arrays.asList(testMarket1, testMarket2);
        when(marketRepository.findAllByOrderByNameAsc()).thenReturn(mockMarkets);
        // Only stub what will definitely be called - getPlacePhotos for markets with Place IDs
        when(googlePlacesService.getPlacePhotos(anyString(), anyInt())).thenReturn(Arrays.asList());

        // When
        List<MarketResponse> result = marketService.getAllMarkets();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Market 1", result.get(0).getName());
        assertEquals("Test Market 2", result.get(1).getName());
        
        verify(marketRepository).findAllByOrderByNameAsc();
    }

    @Test
    @DisplayName("Should get market by ID when found")
    void testGetMarketById_Found() {
        // Given
        when(marketRepository.findById(1L)).thenReturn(Optional.of(testMarket1));
        when(googlePlacesService.getPlacePhotos(anyString(), anyInt())).thenReturn(Arrays.asList());

        // When
        Optional<MarketResponse> result = marketService.getMarketById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Market 1", result.get().getName());
        verify(marketRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when market not found")
    void testGetMarketById_NotFound() {
        // Given
        when(marketRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<MarketResponse> result = marketService.getMarketById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(marketRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get markets by name")
    void testGetMarketsByName() {
        // Given
        List<Market> mockMarkets = Arrays.asList(testMarket1);
        when(marketRepository.findByNameContainingIgnoreCase("Test")).thenReturn(mockMarkets);
        when(googlePlacesService.getPlacePhotos(anyString(), anyInt())).thenReturn(Arrays.asList());

        // When
        List<MarketResponse> result = marketService.getMarketsByName("Test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Market 1", result.get(0).getName());
        verify(marketRepository).findByNameContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("Should get markets within radius")
    void testGetMarketsWithinRadius() {
        // Given
        BigDecimal lat = new BigDecimal("5.4164");
        BigDecimal lon = new BigDecimal("100.3327");
        double radius = 10.0;
        
        List<Market> mockMarkets = Arrays.asList(testMarket1);
        when(marketRepository.findMarketsWithinRadius(lat, lon, radius)).thenReturn(mockMarkets);
        when(googlePlacesService.getPlacePhotos(anyString(), anyInt())).thenReturn(Arrays.asList());

        // When
        List<MarketResponse> result = marketService.getMarketsWithinRadius(lat, lon, radius);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Market 1", result.get(0).getName());
        verify(marketRepository).findMarketsWithinRadius(lat, lon, radius);
    }

    @Test
    @DisplayName("Should get covered markets")
    void testGetCoveredMarkets() {
        // Given
        testMarket1.setIsCovered(true);
        List<Market> mockMarkets = Arrays.asList(testMarket1);
        when(marketRepository.findByIsCoveredTrue()).thenReturn(mockMarkets);
        when(googlePlacesService.getPlacePhotos(anyString(), anyInt())).thenReturn(Arrays.asList());

        // When
        List<MarketResponse> result = marketService.getCoveredMarkets();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsCovered());
        verify(marketRepository).findByIsCoveredTrue();
    }

    @Test
    @DisplayName("Should sync with Google Places successfully")
    void testSyncWithGooglePlaces() {
        // Given
        when(marketRepository.findById(1L)).thenReturn(Optional.of(testMarket1));
        when(marketRepository.save(any(Market.class))).thenReturn(testMarket1);
        doNothing().when(googlePlacesService).updateMarketWithGoogle(any(Market.class));

        // When
        marketService.syncWithGooglePlaces(1L);

        // Then
        verify(marketRepository).findById(1L);
        verify(marketRepository).save(any(Market.class));
    }
}