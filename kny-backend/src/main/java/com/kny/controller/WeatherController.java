package com.kny.controller;

import com.kny.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public String weatherPage(Model model) {
        try {
            WeatherService.WeatherData weather = weatherService.getWeather();
            model.addAttribute("weather", weather);
            return "weather";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading weather: " + e.getMessage());
            return "weather";
        }
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> getWeatherApi() {
        try {
            return ResponseEntity.ok(weatherService.getWeather());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Weather failed: " + e.getMessage());
        }
    }

    @GetMapping("/api/location")
    @ResponseBody
    public ResponseEntity<?> getWeatherForLocation(
            @RequestParam double lat, 
            @RequestParam double lon) {
        try {
            return ResponseEntity.ok(weatherService.getWeather(lat, lon));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Weather failed: " + e.getMessage());
        }
    }
}