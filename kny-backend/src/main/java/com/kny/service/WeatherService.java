package com.kny.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    // Fixed property names to match application.properties
    @Value("${app.openmeteo.api.url:https://api.open-meteo.com/v1/forecast}")
    private String apiUrl;

    @Value("${app.weather.default.latitude:5.4164}")
    private double defaultLat;

    @Value("${app.weather.default.longitude:100.3327}")
    private double defaultLon;

    @Value("${app.weather.timezone:Asia/Kuala_Lumpur}")
    private String timezone;

    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherData getWeather() {
        return getWeather(defaultLat, defaultLon);
    }

    public WeatherData getWeather(double lat, double lon) {
        try {
            // Build OpenMeteo API URL with timezone
            String url = String.format("%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,weather_code&daily=temperature_2m_max,temperature_2m_min,weather_code,precipitation_probability_max&forecast_days=7&timezone=%s",
                    apiUrl, lat, lon, timezone);
            
            System.out.println("Calling OpenMeteo API: " + url);
            
            // Call OpenMeteo API
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("OpenMeteo Response: " + response);
            
            JsonNode root = mapper.readTree(response);
            
            WeatherData weather = new WeatherData();
            
            // Current weather-
            JsonNode current = root.get("current");
            weather.currentTemp = current.get("temperature_2m").asDouble();
            weather.humidity = current.get("relative_humidity_2m").asInt();
            weather.condition = getCondition(current.get("weather_code").asInt());
            
            // Parse time properly and convert to local timezone
            String timeString = current.get("time").asText();
            weather.time = formatTime(timeString);
            
            // Daily forecast
            JsonNode daily = root.get("daily");
            weather.forecast = parseForecast(daily);
            
            System.out.println("Weather data created: " + weather.currentTemp + "°C, " + weather.condition + ", " + weather.time);
            
            return weather;
            
        } catch (Exception e) {
            System.err.println("Error fetching weather: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get weather: " + e.getMessage());
        }
    }

    private String formatTime(String timeString) {
        try {
            // Parse the ISO datetime string and format it nicely
            ZonedDateTime dateTime = ZonedDateTime.parse(timeString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dateTime.format(formatter);
        } catch (Exception e) {
            System.err.println("Error parsing time: " + timeString + " - " + e.getMessage());
            return timeString; // Return original if parsing fails
        }
    }

    private List<DailyWeather> parseForecast(JsonNode daily) {
        List<DailyWeather> forecast = new ArrayList<>();
        JsonNode dates = daily.get("time");
        JsonNode maxTemps = daily.get("temperature_2m_max");
        JsonNode minTemps = daily.get("temperature_2m_min");
        JsonNode rainChance = daily.get("precipitation_probability_max");
        JsonNode weatherCodes = daily.get("weather_code");

        for (int i = 0; i < dates.size(); i++) {
            DailyWeather day = new DailyWeather();
            day.date = dates.get(i).asText();
            day.maxTemp = maxTemps.get(i).asDouble();
            day.minTemp = minTemps.get(i).asDouble();
            day.rainChance = rainChance != null ? rainChance.get(i).asInt() : 0;
            day.condition = getCondition(weatherCodes.get(i).asInt());
            forecast.add(day);
        }
        return forecast;
    }

    private String getCondition(int code) {
        switch (code) {
            case 0: return "Clear";
            case 1: return "Mostly Clear";
            case 2: return "Partly Cloudy";
            case 3: return "Cloudy";
            case 45, 48: return "Foggy";
            case 51, 53, 55: return "Light Rain";
            case 56, 57: return "Freezing Drizzle";
            case 61, 63, 65: return "Rain";
            case 66, 67: return "Freezing Rain";
            case 71, 73, 75: return "Snow";
            case 77: return "Snow Grains";
            case 80, 81, 82: return "Showers";
            case 85, 86: return "Snow Showers";
            case 95: return "Thunderstorm";
            case 96, 99: return "Thunderstorm with Hail";
            default: return "Unknown";
        }
    }

    // Simple data classes
    public static class WeatherData {
        public double currentTemp;
        public int humidity;
        public String condition;
        public String time;
        public List<DailyWeather> forecast;
        
        // Add toString for debugging
        @Override
        public String toString() {
            return String.format("WeatherData{temp=%.1f°C, humidity=%d%%, condition='%s', time='%s', forecast=%d days}", 
                currentTemp, humidity, condition, time, forecast != null ? forecast.size() : 0);
        }
    }

    public static class DailyWeather {
        public String date;
        public double maxTemp;
        public double minTemp;
        public int rainChance;
        public String condition;
        
        // Add toString for debugging
        @Override
        public String toString() {
            return String.format("DailyWeather{date='%s', max=%.1f°C, min=%.1f°C, rain=%d%%, condition='%s'}", 
                date, maxTemp, minTemp, rainChance, condition);
        }
    }
}