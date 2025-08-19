package com.kny.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService Unit Tests")
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "apiUrl", "https://api.open-meteo.com/v1/forecast");
        ReflectionTestUtils.setField(weatherService, "defaultLat", 5.4164);
        ReflectionTestUtils.setField(weatherService, "defaultLon", 100.3327);
        ReflectionTestUtils.setField(weatherService, "timezone", "Asia/Kuala_Lumpur");
    }

    @Test
    @DisplayName("Should get weather successfully")
    void testGetWeather_Success() throws Exception {
        // Given
        String mockResponse = """
            {
                "current": {
                    "temperature_2m": 28.5,
                    "relative_humidity_2m": 75,
                    "weather_code": 1,
                    "time": "2025-07-03T10:00"
                },
                "daily": {
                    "time": ["2025-07-03", "2025-07-04"],
                    "temperature_2m_max": [30.0, 32.0],
                    "temperature_2m_min": [24.0, 26.0],
                    "weather_code": [1, 2],
                    "precipitation_probability_max": [10, 20]
                }
            }
            """;
        
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // When
        WeatherService.WeatherData result = weatherService.getWeather(5.4164, 100.3327);

        // Then
        assertNotNull(result);
        assertEquals(28.5, result.currentTemp);
        assertEquals(75, result.humidity);
        assertEquals("Mostly Clear", result.condition);
        assertNotNull(result.forecast);
        assertEquals(2, result.forecast.size());
        
        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }

    @Test
    @DisplayName("Should handle network error")
    void testGetWeather_NetworkError() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenThrow(new RuntimeException("Network error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeather(5.4164, 100.3327);
        });
        
        assertTrue(exception.getMessage().contains("Failed to get weather"));
        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }

    @Test
    @DisplayName("Should get weather with default location")
    void testGetWeather_DefaultLocation() {
        // Given
        String mockResponse = """
            {
                "current": {
                    "temperature_2m": 29.0,
                    "relative_humidity_2m": 80,
                    "weather_code": 2,
                    "time": "2025-07-03T10:00"
                },
                "daily": {
                    "time": ["2025-07-03"],
                    "temperature_2m_max": [31.0],
                    "temperature_2m_min": [25.0],
                    "weather_code": [2],
                    "precipitation_probability_max": [15]
                }
            }
            """;
        
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // When
        WeatherService.WeatherData result = weatherService.getWeather();

        // Then
        assertNotNull(result);
        assertEquals(29.0, result.currentTemp);
        assertEquals("Partly Cloudy", result.condition);
        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }
}