import React, { useState, useEffect } from 'react';
import { useWeather } from '../hooks/useWeather';
import '../styles/WeatherPage.css';

const WeatherPage = () => {
  const { weather, forecast, loading, error, refetch } = useWeather();

  const weatherTips = {
    sunny: [
      'Visit early morning (6-9 AM)',
      'Bring sun protection',
      'Stay hydrated'
    ],
    rainy: [
      'Choose covered markets',
      'Bring umbrella',
      'Check opening hours'
    ],
    hot: [
      'Visit before 10 AM',
      'Seek shaded areas',
      'Take frequent breaks'
    ]
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { weekday: 'short' });
  };

  const getWeatherAlert = () => {
    if (!forecast || forecast.length === 0) return null;
    
    const tomorrow = forecast[1];
    if (tomorrow && tomorrow.rainChance > 50) {
      return {
        type: 'rain',
        message: `${tomorrow.condition} expected tomorrow (${tomorrow.rainChance}% chance of rain). Consider covered markets or visit during dry periods.`
      };
    }
    return null;
  };

  const weatherAlert = getWeatherAlert();

  if (loading) {
    return (
      <div className="weather-page">
        <div className="loading">Loading weather data...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="weather-page">
        <div className="error">
          <h2>Weather Service Error</h2>
          <p>{error}</p>
          <button onClick={refetch}>Try Again</button>
        </div>
      </div>
    );
  }

  return (
    <div className="weather-page">
      <div className="current-weather-card">
        <div className="weather-content">
          <h1>Current Weather in Penang</h1>
          <div className="weather-display">
            {weather ? (
              <div className="weather-data">
                <div className="temperature">{Math.round(weather.currentTemp)}°C</div>
                <div className="condition">{weather.condition}</div>
                <div className="weather-details">
                  <span>Humidity: {weather.humidity}%</span>
                  {weather.time && <span>Updated: {new Date(weather.time).toLocaleString()}</span>}
                </div>
              </div>
            ) : (
              <div className="error-weather">
                <div className="temperature">N/A</div>
                <div className="condition">Unable to load weather</div>
              </div>
            )}
          </div>
        </div>
        
        <div className="weather-description">
          <p>
            {weather ? 
              `Current temperature is ${Math.round(weather.currentTemp)}°C with ${weather.condition.toLowerCase()} conditions. Humidity: ${weather.humidity}%` :
              'Unable to fetch weather data. Please try again later.'
            }
          </p>
        </div>
      </div>

      <div className="forecast-section">
        <h2>7 Day Weather Forecast</h2>
        <div className="forecast-grid">
          {forecast && forecast.map((day, index) => (
            <div key={index} className="forecast-item">
              <div className="day">
                {index === 0 ? 'Today' : 
                 index === 1 ? 'Tomorrow' : 
                 formatDate(day.date)}
              </div>
              <div className="weather-info">
                {Math.round(day.maxTemp)}°/{Math.round(day.minTemp)}°C
              </div>
              <div className="condition">{day.condition}</div>
              <div className="rain">{day.rainChance}% chance</div>
            </div>
          ))}
        </div>
      </div>

      {weatherAlert && (
        <div className="weather-alert">
          <div className="alert-icon">⚠️</div>
          <div className="alert-content">
            <strong>Tomorrow's Weather Alert:</strong>
            <p>{weatherAlert.message}</p>
          </div>
        </div>
      )}

      <div className="weather-tips">
        <div className="tips-header">
          <span className="tips-icon">💡</span>
          <span>Weather Tips for Market Visits</span>
        </div>
        
        <div className="tips-grid">
          <div className="tip-category">
            <div className="tip-icon">☀️</div>
            <h4>Sunny Days</h4>
            <ul>
              {weatherTips.sunny.map((tip, index) => (
                <li key={index}>{tip}</li>
              ))}
            </ul>
          </div>
          
          <div className="tip-category">
            <div className="tip-icon">🌧️</div>
            <h4>Rainy Days</h4>
            <ul>
              {weatherTips.rainy.map((tip, index) => (
                <li key={index}>{tip}</li>
              ))}
            </ul>
          </div>
          
          <div className="tip-category">
            <div className="tip-icon">🌡️</div>
            <h4>Hot Weather</h4>
            <ul>
              {weatherTips.hot.map((tip, index) => (
                <li key={index}>{tip}</li>
              ))}
            </ul>
          </div>
        </div>
      </div>
      
      <div className="refresh-section">
        <button onClick={refetch} className="refresh-btn">
          🔄 Refresh Weather Data
        </button>
      </div>
    </div>
  );
};

export default WeatherPage;