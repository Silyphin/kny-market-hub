// src/services/marketService.js
import apiService from './api';

class MarketService {
  // Get all markets
  async getAllMarkets() {
    try {
      return await apiService.get('/api/markets');
    } catch (error) {
      throw new Error('Failed to fetch markets');
    }
  }

  // Get market by ID
  async getMarketById(id) {
    try {
      return await apiService.get(`/api/markets/${id}`);
    } catch (error) {
      throw new Error(`Failed to fetch market with ID ${id}`);
    }
  }

  // Get nearby markets
  async getNearbyMarkets(latitude, longitude, radius = 10.0) {
    try {
      return await apiService.get('/api/markets/nearby', {
        latitude,
        longitude,
        radius
      });
    } catch (error) {
      throw new Error('Failed to fetch nearby markets');
    }
  }

  // Get covered markets only
  async getCoveredMarkets() {
    try {
      return await apiService.get('/api/markets/covered');
    } catch (error) {
      throw new Error('Failed to fetch covered markets');
    }
  }

  // Get markets by crowd level
  async getMarketsByCrowdLevel(timeOfDay, crowdLevel) {
    try {
      return await apiService.get('/api/markets/crowd-level', {
        timeOfDay,
        crowdLevel
      });
    } catch (error) {
      throw new Error('Failed to fetch markets by crowd level');
    }
  }

  // Get high-rated markets
  async getHighRatedMarkets(minRating = 4.0) {
    try {
      return await apiService.get('/api/markets/high-rated', {
        minRating
      });
    } catch (error) {
      throw new Error('Failed to fetch high-rated markets');
    }
  }

  // Get market statistics
  async getMarketStatistics() {
    try {
      return await apiService.get('/api/markets/statistics');
    } catch (error) {
      throw new Error('Failed to fetch market statistics');
    }
  }

  // Sync market with Google Places
  async syncMarketWithGoogle(id) {
    try {
      return await apiService.post(`/api/markets/${id}/sync`);
    } catch (error) {
      throw new Error(`Failed to sync market ${id} with Google`);
    }
  }

  // Health check
  async healthCheck() {
    try {
      return await apiService.get('/api/markets/health');
    } catch (error) {
      throw new Error('Market service health check failed');
    }
  }
}

const marketService = new MarketService();
export default marketService;