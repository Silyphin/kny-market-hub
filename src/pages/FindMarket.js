// Updated FindMarket.js - Remove rating display completely
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMarkets } from '../hooks/useMarkets';
import '../styles/FindMarket.css';

const FindMarket = () => {
  const navigate = useNavigate();
  const { markets, loading, error, refetch } = useMarkets();

  // Format time helper
  const formatTime = (timeStr) => {
    if (!timeStr) return 'Not available';
    
    const [hours, minutes] = timeStr.split(':').map(Number);
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const displayHour = hours === 0 ? 12 : hours > 12 ? hours - 12 : hours;
    const displayMinutes = minutes.toString().padStart(2, '0');
    return `${displayHour}:${displayMinutes} ${ampm}`;
  };

  // Format market hours
  const formatMarketHours = (market) => {
    if (!market.openingTime || !market.closingTime) {
      return 'Hours not available';
    }
    
    const openStatus = market.isOpen === true ? 'Open' : 
                      market.isOpen === false ? 'Closed' : '';
    
    return `${openStatus ? openStatus + ': ' : ''}${formatTime(market.openingTime)} - ${formatTime(market.closingTime)}`;
  };

  // Get crowd level emoji
  const getCrowdLevelEmoji = (level) => {
    const levelStr = typeof level === 'string' ? level.toUpperCase() : level?.toString()?.toUpperCase() || 'MEDIUM';
    switch (levelStr) {
      case 'HIGH': return '🔴 Busy';
      case 'MEDIUM': return '🟡 Moderate';
      case 'LOW': return '🟢 Quiet';
      default: return '🟡 Moderate';
    }
  };

  // Get current crowd level
  const getCurrentCrowdLevel = (market) => {
    if (market.currentCrowdLevel) {
      return market.currentCrowdLevel.toUpperCase();
    }
    
    const hour = new Date().getHours();
    if (hour >= 6 && hour < 12) {
      return market.crowdLevelMorning || 'MEDIUM';
    } else if (hour >= 12 && hour < 18) {
      return market.crowdLevelAfternoon || 'MEDIUM';
    } else {
      return market.crowdLevelEvening || 'LOW';
    }
  };

  const handleViewDetails = (marketId) => {
    navigate(`/market/${marketId}`);
  };

  const handleRetry = () => {
    refetch();
  };

  if (loading) {
    return (
      <div className="find-market">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading markets...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="find-market">
      {error && (
        <div className="error-container">
          <p className="error-message">⚠️ {error}</p>
          <button onClick={handleRetry} className="retry-btn">
            Try Again
          </button>
        </div>
      )}

      <div className="available-markets">
        <h2>Available Markets ({markets.length})</h2>
        {markets.length === 0 && !loading ? (
          <div className="no-markets">
            <p>No markets found. Please check your backend database.</p>
            <button onClick={handleRetry} className="retry-btn">
              Refresh
            </button>
          </div>
        ) : (
          <div className="markets-list">
            {markets.map(market => (
              <div key={market.id} className="market-item">
                <div className="market-header">
                  <div className="market-title">
                    <h3>{market.name}</h3>
                    <div className="market-badges">
                      {/* REMOVED: Rating display - no longer showing ratings */}
                      
                      {market.isCovered && (
                        <span className="covered-badge">🏠 Covered</span>
                      )}
                    </div>
                  </div>
                  <button 
                    onClick={() => handleViewDetails(market.id)}
                    className="view-details-btn"
                  >
                    View Details
                  </button>
                </div>
                
                <div className="market-info">
                  <div className="location-info">
                    <span className="address">📍 {market.address}</span>
                  </div>
                  
                  <div className="hours">
                    🕒 {formatMarketHours(market)}
                  </div>
                  
                  <div className="crowd-level">
                    👥 {getCrowdLevelEmoji(getCurrentCrowdLevel(market))}
                  </div>
                  
                  {market.specialties && (
                    <div className="specialties">
                      🍜 {market.specialties}
                    </div>
                  )}

                  {/* Keep photos - they're still free */}
                  {market.photos && market.photos.length > 0 && (
                    <div className="market-photos">
                      <div className="photo-slider">
                        {market.photos.map((photo, index) => (
                          <div 
                            key={index}
                            className="photo-slide"
                            style={{ '--index': index }}
                          >
                            <img 
                              src={photo.photoUrl} 
                              alt={`${market.name} photo ${index + 1}`}
                              className="market-photo"
                              onError={(e) => {
                                e.target.style.display = 'none';
                              }}
                            />
                          </div>
                        ))}
                        {market.photos.length > 1 && (
                          <div className="photo-indicators">
                            {market.photos.map((_, index) => (
                              <span 
                                key={index}
                                className={`indicator ${index === 0 ? 'active' : ''}`}
                              />
                            ))}
                          </div>
                        )}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default FindMarket;