import { useState, useEffect } from 'react';
import marketService from '../services/marketService';

export const useMarkets = (searchParams = {}) => {
  const [markets, setMarkets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchMarkets = async () => {
    try {
      setLoading(true);
      setError(null);
      
      let data;
      
      // Match your backend endpoints
      if (searchParams.name || searchParams.specialty) {
        // Use search endpoint
        data = await marketService.searchMarkets(searchParams);
      } else if (searchParams.latitude && searchParams.longitude) {
        // Use nearby endpoint
        data = await marketService.getNearbyMarkets(
          searchParams.latitude, 
          searchParams.longitude, 
          searchParams.radius || 10.0
        );
      } else if (searchParams.covered) {
        // Get covered markets only
        data = await marketService.getCoveredMarkets();
      } else if (searchParams.minRating) {
        // Get high-rated markets
        data = await marketService.getHighRatedMarkets(searchParams.minRating);
      } else {
        // Get all markets (default)
        data = await marketService.getAllMarkets();
      }
      
      setMarkets(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMarkets();
  }, [searchParams.name, searchParams.specialty, searchParams.latitude, searchParams.longitude, searchParams.covered, searchParams.minRating]);

  const searchMarkets = async (params) => {
    try {
      setLoading(true);
      setError(null);
      const data = await marketService.searchMarkets(params);
      setMarkets(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const getNearbyMarkets = async (lat, lon, radius = 10.0) => {
    try {
      setLoading(true);
      setError(null);
      const data = await marketService.getNearbyMarkets(lat, lon, radius);
      setMarkets(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const getMarketsByCrowdLevel = async (timeOfDay, crowdLevel) => {
    try {
      setLoading(true);
      setError(null);
      const data = await marketService.getMarketsByCrowdLevel(timeOfDay, crowdLevel);
      setMarkets(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return { 
    markets, 
    loading, 
    error, 
    searchMarkets,
    getNearbyMarkets,
    getMarketsByCrowdLevel,
    refetch: fetchMarkets 
  };
};