// Updated HomePage.js - Remove rating display
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMarkets } from '../hooks/useMarkets';
import { useWeather } from '../hooks/useWeather';
import '../styles/HomePage.css';

const HomePage = () => {
  const navigate = useNavigate();
  const { markets, loading: marketsLoading } = useMarkets();
  const { weather, forecast, loading: weatherLoading } = useWeather();
  
  const [popularMarkets, setPopularMarkets] = useState([]);

  useEffect(() => {
    if (markets && markets.length > 0) {
      processPopularMarkets();
    }
  }, [markets]);

  const processPopularMarkets = () => {
    const popularMarketIds = [1, 3, 4, 5, 10];
    let popularSelection = markets
      .filter(market => popularMarketIds.includes(market.id))
      .sort((a, b) => {
        const aOpen = a.isOpen ? 1 : 0;
        const bOpen = b.isOpen ? 1 : 0;
        if (aOpen !== bOpen) return bOpen - aOpen;
        
        const crowdWeight = { 'HIGH': 3, 'MEDIUM': 2, 'LOW': 1 };
        const aCrowd = getCurrentCrowdLevel(a.crowdLevel);
        const bCrowd = getCurrentCrowdLevel(b.crowdLevel);
        if (crowdWeight[aCrowd] !== crowdWeight[bCrowd]) {
          return crowdWeight[bCrowd] - crowdWeight[aCrowd];
        }
        // REMOVED: Rating-based sorting since we're not showing ratings
        return 0;
      })
      .slice(0, 4);

    if (popularSelection.length < 4) {
      const remainingMarkets = markets
        .filter(market => !popularMarketIds.includes(market.id))
        .filter(market => market.isOpen)
        .slice(0, 4 - popularSelection.length);
      popularSelection = [...popularSelection, ...remainingMarkets];
    }
    
    setPopularMarkets(popularSelection);
  };

  const getCurrentCrowdLevel = (crowdLevel) => {
    if (!crowdLevel) return 'MEDIUM';
    const hour = new Date().getHours();
    if (hour >= 6 && hour < 12) return crowdLevel.morning || 'MEDIUM';
    if (hour >= 12 && hour < 18) return crowdLevel.afternoon || 'MEDIUM';
    return crowdLevel.evening || 'LOW';
  };

  const getCrowdLevelDisplay = (market) => {
    if (!market.isOpen) return { text: 'Closed', color: 'gray' };
    const currentLevel = getCurrentCrowdLevel(market.crowdLevel);
    const levels = {
      'HIGH': { text: 'Busy Now', color: 'red' },
      'MEDIUM': { text: 'Moderate', color: 'orange' },
      'LOW': { text: 'Quiet Now', color: 'green' }
    };
    return levels[currentLevel] || { text: 'Moderate', color: 'orange' };
  };

  const formatMarketHours = (openingTime, closingTime) => {
    if (!openingTime || !closingTime) return 'Hours not available';
    const formatTime = (timeStr) => {
      const [hours, minutes] = timeStr.split(':').map(Number);
      const ampm = hours >= 12 ? 'PM' : 'AM';
      const displayHour = hours === 0 ? 12 : hours > 12 ? hours - 12 : hours;
      return `${displayHour}:${minutes.toString().padStart(2, '0')} ${ampm}`;
    };
    return `${formatTime(openingTime)} - ${formatTime(closingTime)}`;
  };

  const getWeatherBasedTip = () => {
    if (weatherLoading || !weather) {
      return { title: 'Market Tip:', content: 'Check weather conditions for the best market experience.' };
    }
    
    const temp = weather.currentTemp;
    const condition = weather.condition.toLowerCase();
    
    if (condition.includes('rain')) {
      return {
        title: 'Weather Alert:',
        content: `It's raining in Penang. Consider visiting covered markets or wait for the rain to subside.`
      };
    } else if (temp > 32) {
      return {
        title: 'Hot Weather Tip:',
        content: `It's hot today at ${Math.round(temp)}°C. Visit markets early morning (6-9 AM) or late afternoon.`
      };
    } else if (temp < 25) {
      return {
        title: 'Cool Weather Tip:',
        content: `Perfect weather for market visits! At ${Math.round(temp)}°C, it's comfortable all day.`
      };
    } else {
      return {
        title: 'Great Weather Tip:',
        content: `Excellent weather for market visits! Current temperature is ${Math.round(temp)}°C.`
      };
    }
  };

  const currentTip = getWeatherBasedTip();

  return (
    <div className="homepage">
      <div className="weather-section">
        <div className="weather-info">
          <h2>Current Weather in Penang</h2>
          <div className="weather-display">
            {weatherLoading ? (
              <div className="loading-weather">
                <div className="temperature">Loading...</div>
                <div className="condition">Fetching weather data...</div>
              </div>
            ) : weather ? (
              <div className="weather-data">
                <div className="temperature">{Math.round(weather.currentTemp)}°C</div>
                <div className="condition">{weather.condition}</div>
                <div className="weather-details">
                  <span>Humidity: {weather.humidity}%</span>
                  {weather.time && <span>Updated: {new Date(weather.time).toLocaleTimeString()}</span>}
                </div>
              </div>
            ) : (
              <div className="error-weather">
                <div className="temperature">Error</div>
                <div className="condition">Unable to load weather</div>
              </div>
            )}
          </div>
        </div>
        
        <div className="market-tip">
          <h3>{currentTip.title}</h3>
          <p>{currentTip.content}</p>
        </div>
      </div>

      <div className="action-buttons">
        <button 
          className="btn btn-primary" 
          onClick={() => navigate('/find-market')}
        >
          Find Market Near Me
        </button>
        <button 
          className="btn btn-primary" 
          onClick={() => navigate('/weather')}
        >
          Check Best Time
        </button>
      </div>

      <div className="popular-markets">
        <div className="popular-markets-header">
          <h2>Popular Markets Right Now</h2>
          <div className="last-updated">
            <span>🔄 Live updates every 5 minutes</span>
          </div>
        </div>
        
        {marketsLoading ? (
          <div className="markets-loading">
            <div className="loading-spinner"></div>
            <p>Loading popular markets...</p>
          </div>
        ) : (
          <div className="markets-grid">
            {popularMarkets.map(market => {
              const crowdInfo = getCrowdLevelDisplay(market);
              return (
                <div key={market.id} className="market-card">
                  <div className="market-card-header">
                    <h3>{market.name}</h3>
                    <div className="market-status">
                      {market.isOpen ? (
                        <span className="status-open">🟢 Open</span>
                      ) : (
                        <span className="status-closed">🔴 Closed</span>
                      )}
                      {market.isCovered && <span className="covered-badge">🏠 Covered</span>}
                    </div>
                  </div>
                  
                  <p className="market-description">{market.description}</p>
                  
                  <div className="market-details">
                    <div className="specialties">
                      <strong>Specialties:</strong> {market.specialties}
                    </div>
                    
                    {market.openingTime && market.closingTime && (
                      <div className="hours">
                        <strong>Hours:</strong> {formatMarketHours(market.openingTime, market.closingTime)}
                      </div>
                    )}
                    
                    {/* REMOVED: Rating display since it costs money */}
                  </div>
                  
                  <div className="market-card-footer">
                    <button 
                      className={`crowd-btn ${crowdInfo.color}`}
                      disabled={!market.isOpen}
                    >
                      👥 {crowdInfo.text}
                    </button>
                    <button 
                      className="view-details-btn"
                      onClick={() => navigate(`/market/${market.id}`)}
                    >
                      View Details
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
        
        {!marketsLoading && popularMarkets.length === 0 && (
          <div className="no-markets">
            <p>No popular markets data available at the moment.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;