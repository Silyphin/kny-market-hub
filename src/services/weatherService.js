// src/services/weatherService.js
import apiService from './api';

class WeatherService {
  // Get current weather (matches backend /weather/api)
  async getCurrentWeather() {
    try {
      return await apiService.get('/weather/api');
    } catch (error) {
      throw new Error('Failed to fetch current weather');
    }
  }

  // Get weather for specific location (matches backend /weather/api/location)
  async getWeatherForLocation(lat, lon) {
    try {
      return await apiService.get('/weather/api/location', { lat, lon });
    } catch (error) {
      throw new Error('Failed to fetch weather for location');
    }
  }
}

const weatherService = new WeatherService();
export default weatherService;