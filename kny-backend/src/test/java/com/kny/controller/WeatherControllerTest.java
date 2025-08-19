package com.kny.controller;

import com.kny.service.WeatherService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherController Unit Tests")
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private WeatherService.WeatherData weatherData;

    @BeforeEach
    void setUp() {
        // Create MockMvc without Spring context
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
        objectMapper = new ObjectMapper();
        
        weatherData = new WeatherService.WeatherData();
        weatherData.currentTemp = 28.5;
        weatherData.humidity = 75;
        weatherData.condition = "Partly Cloudy";
        weatherData.time = "2025-07-03T10:00:00";
        
        WeatherService.DailyWeather dailyWeather = new WeatherService.DailyWeather();
        dailyWeather.date = "2025-07-03";
        dailyWeather.maxTemp = 30.0;
        dailyWeather.minTemp = 24.0;
        dailyWeather.condition = "Sunny";
        dailyWeather.rainChance = 10;
        
        weatherData.forecast = Arrays.asList(dailyWeather);
    }

    @Test
    @DisplayName("Should get weather successfully")
    void testGetWeatherApi_Success() throws Exception {
        // Given
        when(weatherService.getWeather()).thenReturn(weatherData);

        // When & Then
        mockMvc.perform(get("/weather/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTemp").value(28.5))
                .andExpect(jsonPath("$.humidity").value(75))
                .andExpect(jsonPath("$.condition").value("Partly Cloudy"))
                .andExpect(jsonPath("$.forecast").isArray())
                .andExpect(jsonPath("$.forecast[0].maxTemp").value(30.0));

        verify(weatherService).getWeather();
    }

    @Test
    @DisplayName("Should handle weather service error")
    void testGetWeatherApi_ServiceError() throws Exception {
        // Given
        when(weatherService.getWeather()).thenThrow(new RuntimeException("Weather API unavailable"));

        // When & Then
        mockMvc.perform(get("/weather/api"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Weather failed: Weather API unavailable"));

        verify(weatherService).getWeather();
    }

    @Test
    @DisplayName("Should get weather for specific location")
    void testGetWeatherForLocation() throws Exception {
        // Given
        when(weatherService.getWeather(5.4164, 100.3327)).thenReturn(weatherData);

        // When & Then
        mockMvc.perform(get("/weather/api/location")
                .param("lat", "5.4164")
                .param("lon", "100.3327"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTemp").value(28.5))
                .andExpect(jsonPath("$.condition").value("Partly Cloudy"));

        verify(weatherService).getWeather(5.4164, 100.3327);
    }
}