import { useState, useEffect } from 'react';
import weatherService from '../services/weatherService';

export const useWeather = () => {
  const [weather, setWeather] = useState(null);
  const [forecast, setForecast] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchWeather = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Use the correct method that matches your backend
      const weatherData = await weatherService.getCurrentWeather();
      
      // Backend returns { currentTemp, humidity, condition, time, forecast }
      setWeather({
        currentTemp: weatherData.currentTemp,
        humidity: weatherData.humidity,
        condition: weatherData.condition,
        time: weatherData.time
      });
      
      // Backend already includes 7-day forecast in the response
      setForecast(weatherData.forecast || []);
      
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchWeatherForLocation = async (lat, lon) => {
    try {
      setLoading(true);
      setError(null);
      
      const weatherData = await weatherService.getWeatherForLocation(lat, lon);
      
      setWeather({
        currentTemp: weatherData.currentTemp,
        humidity: weatherData.humidity,
        condition: weatherData.condition,
        time: weatherData.time
      });
      
      setForecast(weatherData.forecast || []);
      
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWeather();
  }, []);

  return { 
    weather, 
    forecast, 
    loading, 
    error, 
    refetch: fetchWeather,
    fetchWeatherForLocation
  };
};