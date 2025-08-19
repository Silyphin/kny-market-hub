import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import marketService from '../services/marketService';
import '../styles/MarketDetails.css';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix for Leaflet marker icons
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

const MarketDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [market, setMarket] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPhotoIndex, setCurrentPhotoIndex] = useState(0);

  useEffect(() => {
    if (id) fetchMarketDetails(id);
  }, [id]);

  const fetchMarketDetails = async (marketId) => {
    try {
      setLoading(true);
      setError(null);
      const marketData = await marketService.getMarketById(marketId);
      setMarket(marketData);
    } catch (err) {
      setError(err.message || 'Failed to load market details');
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (timeStr) => {
    if (!timeStr) return 'Not available';
    
    let time;
    if (typeof timeStr === 'string') {
      const [hours, minutes] = timeStr.split(':').map(Number);
      time = { hours, minutes };
    } else if (timeStr.hour !== undefined) {
      time = { hours: timeStr.hour, minutes: timeStr.minute };
    } else {
      return timeStr;
    }
    
    const ampm = time.hours >= 12 ? 'PM' : 'AM';
    const displayHour = time.hours === 0 ? 12 : time.hours > 12 ? time.hours - 12 : time.hours;
    const displayMinutes = time.minutes.toString().padStart(2, '0');
    return `${displayHour}:${displayMinutes} ${ampm}`;
  };

  const getCrowdLevelEmoji = (level) => {
    const levelStr = (level || 'medium').toString().toUpperCase();
    switch (levelStr) {
      case 'HIGH': return '🔴 Busy';
      case 'MEDIUM': return '🟡 Moderate';
      case 'LOW': return '🟢 Quiet';
      default: return '🟡 Moderate';
    }
  };

  const getCrowdLevelStatus = (market) => {
    if (!market) return 'Status unknown';
    const currentLevel = market.currentCrowdLevel || 'medium';
    const emoji = getCrowdLevelEmoji(currentLevel);
    
    switch (currentLevel.toUpperCase()) {
      case 'LOW': return `${emoji} - Great time to visit!`;
      case 'MEDIUM': return `${emoji} - Moderately busy`;
      case 'HIGH': return `${emoji} - Very busy, consider visiting later`;
      default: return `${emoji} - Current status`;
    }
  };

  const handlePhotoNav = (direction) => {
    if (!market?.photos?.length > 1) return;
    setCurrentPhotoIndex(prev => 
      direction === 'next' 
        ? (prev === market.photos.length - 1 ? 0 : prev + 1)
        : (prev === 0 ? market.photos.length - 1 : prev - 1)
    );
  };

  const handleSyncWithGoogle = async () => {
    try {
      await marketService.syncMarketWithGoogle(market.id);
      await fetchMarketDetails(market.id);
      alert('Market synced with Google Places successfully!');
    } catch (error) {
      alert('Failed to sync with Google Places: ' + error.message);
    }
  };

  const handleBackToMarkets = () => {
    navigate('/find-market');
  };

  // Map component
  const OpenStreetMap = ({ latitude, longitude, name, address }) => {
    const mapRef = useRef(null);
    const mapInstanceRef = useRef(null);

    useEffect(() => {
      if (!latitude || !longitude || !mapRef.current) return;

      if (!mapInstanceRef.current) {
        mapInstanceRef.current = L.map(mapRef.current).setView([latitude, longitude], 15);
        
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors',
          maxZoom: 19
        }).addTo(mapInstanceRef.current);
        
        const marketIcon = L.icon({
          iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
          iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
          shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
          iconSize: [25, 41],
          iconAnchor: [12, 41],
          popupAnchor: [1, -34],
          shadowSize: [41, 41]
        });
        
        L.marker([latitude, longitude], { icon: marketIcon })
          .addTo(mapInstanceRef.current)
          .bindPopup(`<b>${name}</b><br/>${address}`)
          .openPopup();
      }

      return () => {
        if (mapInstanceRef.current) {
          mapInstanceRef.current.remove();
          mapInstanceRef.current = null;
        }
      };
    }, [latitude, longitude, name, address]);

    if (!latitude || !longitude) {
      return (
        <div className="map-placeholder">
          <span>📍 Location: {address}</span>
          <p>Coordinates not available</p>
        </div>
      );
    }

    return (
      <div className="map-container">
        <div ref={mapRef} style={{ height: '400px', width: '100%', borderRadius: '12px', border: '1px solid #ddd' }} />
        <div className="map-actions">
          <a href={`https://www.google.com/maps/dir/?api=1&destination=${latitude},${longitude}`} target="_blank" rel="noopener noreferrer" className="directions-btn">
            🧭 Get Directions
          </a>
          <button onClick={() => window.open(`https://www.openstreetmap.org/?mlat=${latitude}&mlon=${longitude}&zoom=15`, '_blank')} className="view-on-map-btn">
            🗺️ View on OpenStreetMap
          </button>
        </div>
      </div>
    );
  };

  if (loading) {
    return (
      <div className="market-detail">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading market details...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="market-detail">
        <div className="error-container">
          <p className="error-message">⚠️ {error}</p>
          <button onClick={() => fetchMarketDetails(id)} className="retry-btn">Try Again</button>
          <button onClick={handleBackToMarkets} className="back-btn">Back to Markets</button>
        </div>
      </div>
    );
  }

  if (!market) {
    return (
      <div className="market-detail">
        <div className="error-container">
          <p className="error-message">Market not found</p>
          <button onClick={handleBackToMarkets} className="back-btn">Back to Markets</button>
        </div>
      </div>
    );
  }

  return (
    <div className="market-detail">
      <div className="back-navigation">
        <button onClick={handleBackToMarkets} className="back-btn">← Back to Markets</button>
      </div>

      <div className="market-header">
        <h1>{market.name}</h1>
        <div className="market-meta">
          <div className="location">📍 <span>Location:</span> {market.address}</div>
          
          <div className={`status-badge ${market.currentCrowdLevel === 'low' ? 'quiet' : market.currentCrowdLevel === 'high' ? 'busy' : 'moderate'}`}>
            {getCrowdLevelStatus(market)}
          </div>

          {market.isOpen !== null && (
            <div className={`open-status ${market.isOpen ? 'open' : 'closed'}`}>
              {market.isOpen ? '🟢 Open Now' : '🔴 Closed'}
            </div>
          )}
        </div>
      </div>

      <div className="photo-section">
        <div className="photo-container">
          {market.photos?.length > 0 ? (
            <>
              <div className="photo-display">
                <img 
                  src={market.photos[currentPhotoIndex].photoUrl} 
                  alt={`${market.name} photo ${currentPhotoIndex + 1}`} 
                  className="market-photo-large"
                  onError={(e) => { 
                    e.target.style.display = 'none'; 
                    e.target.nextSibling.style.display = 'flex'; 
                  }} 
                />
                <div className="photo-placeholder" style={{display: 'none'}}>
                  <span>Photo unavailable</span>
                </div>
                
                {market.photos.length > 1 && (
                  <>
                    <button className="photo-nav prev" onClick={() => handlePhotoNav('prev')}>‹</button>
                    <button className="photo-nav next" onClick={() => handlePhotoNav('next')}>›</button>
                  </>
                )}
              </div>
              
              {market.photos.length > 1 && (
                <div className="photo-indicators">
                  {market.photos.map((_, index) => (
                    <button
                      key={index}
                      className={`indicator ${index === currentPhotoIndex ? 'active' : ''}`}
                      onClick={() => setCurrentPhotoIndex(index)}
                    />
                  ))}
                </div>
              )}
            </>
          ) : (
            <div className="photo-placeholder">
              <span>No photos available</span>
            </div>
          )}
        </div>
      </div>

      <div className="content-grid">
        <div className="left-column">
          <div className="info-section">
            <h2>Market Information</h2>
            
            <div className="info-row">
              <strong>Operating Hours:</strong>
              <span>
                {market.openingTime && market.closingTime 
                  ? `${formatTime(market.openingTime)} - ${formatTime(market.closingTime)}`
                  : 'Hours not available'
                }
              </span>
            </div>

            {market.description && (
              <div className="info-row">
                <strong>Description:</strong>
                <p>{market.description}</p>
              </div>
            )}

            {market.isCovered && (
              <div className="info-row">
                <strong>Facilities:</strong>
                <span className="covered-badge">🏠 Covered Market</span>
              </div>
            )}

            {market.phoneNumber && (
              <div className="info-row">
                <strong>Phone:</strong>
                <a href={`tel:${market.phoneNumber}`}>{market.phoneNumber}</a>
              </div>
            )}

            {market.website && (
              <div className="info-row">
                <strong>Website:</strong>
                <a href={market.website} target="_blank" rel="noopener noreferrer">
                  {market.website}
                </a>
              </div>
            )}
          </div>

          {market.specialties && (
            <div className="info-section">
              <h2>Specialties & Highlights</h2>
              <p>{market.specialties}</p>
            </div>
          )}

          {market.highlights && (
            <div className="info-section">
              <h2>What Makes This Market Special</h2>
              <p>{market.highlights}</p>
            </div>
          )}
        </div>

        <div className="right-column">
          <div className="prediction-section">
            <h2>Crowd Level Prediction</h2>
            <div className="crowd-schedule">
              <div className="time-slot">
                <span className="time-label">Morning (6AM - 12PM):</span>
                <span className="crowd-level">{getCrowdLevelEmoji(market.crowdLevelMorning)}</span>
              </div>
              <div className="time-slot">
                <span className="time-label">Afternoon (12PM - 6PM):</span>
                <span className="crowd-level">{getCrowdLevelEmoji(market.crowdLevelAfternoon)}</span>
              </div>
              <div className="time-slot">
                <span className="time-label">Evening (6PM - 10PM):</span>
                <span className="crowd-level">{getCrowdLevelEmoji(market.crowdLevelEvening)}</span>
              </div>
            </div>
            
            <div className="current-status">
              <strong>Current Status:</strong> {getCrowdLevelStatus(market)}
            </div>
          </div>

          <div className="data-source-section">
            <h2>Data Information</h2>
            <div className="data-info">
              <div className="info-row">
                <strong>Data Source:</strong>
                <span className={`source-badge ${market.dataSource?.toLowerCase()}`}>
                  {market.dataSource || 'HYBRID'}
                </span>
              </div>
              
              {market.googlePlaceId && (
                <div className="info-row">
                  <strong>Google Places:</strong>
                  <span>✅ Connected</span>
                </div>
              )}
              
              <button onClick={handleSyncWithGoogle} className="sync-btn">
                🔄 Sync with Google
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="map-section">
        <h2>Location & Directions</h2>
        <OpenStreetMap 
          latitude={market.latitude} 
          longitude={market.longitude} 
          name={market.name}
          address={market.address}
        />
      </div>
    </div>
  );
};

export default MarketDetail;