// src/services/authService.js
import apiService from './api';

class AuthService {
  // Register new user
  async register(registrationData) {
    try {
      const response = await apiService.post('/api/auth/register', {
        name: `${registrationData.firstName} ${registrationData.lastName}`,
        email: registrationData.email,
        password: registrationData.password
      });
      return response;
    } catch (error) {
      throw new Error(error.message || 'Registration failed');
    }
  }

  // Login with email and password
  async login(loginData) {
    try {
      console.log('AuthService login called with:', loginData); // DEBUG
      const response = await apiService.post('/api/auth/login', loginData);
      console.log('AuthService login response:', response); // DEBUG
      return response;
    } catch (error) {
      console.error('AuthService login error:', error); // DEBUG
      throw new Error(error.message || 'Login failed');
    }
  }

  // Get current authenticated user info
  async getCurrentUser() {
    try {
      console.log('AuthService getCurrentUser called'); // DEBUG
      const response = await apiService.get('/api/auth/user');
      console.log('AuthService getCurrentUser response:', response); // DEBUG
      return response;
    } catch (error) {
      console.error('AuthService getCurrentUser error:', error); // DEBUG
      throw new Error('Failed to get current user');
    }
  }

  // Check if user is authenticated
  async isAuthenticated() {
    try {
      await this.getCurrentUser();
      return true;
    } catch (error) {
      return false;
    }
  }

  // Logout
  async logout() {
    try {
      // Make API call to logout endpoint first
      await apiService.post('/api/auth/logout');
    } catch (error) {
      console.error('Logout API error:', error);
    } finally {
      // Always redirect to clear frontend state
      window.location.href = '/';
    }
  }

  // OAuth login redirects
  initiateGoogleLogin() {
    const baseURL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
    window.location.href = `${baseURL}/oauth2/authorization/google`;
  }

  initiateFacebookLogin() {
    const baseURL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
    window.location.href = `${baseURL}/oauth2/authorization/facebook`;  
  }
}

const authService = new AuthService();
export default authService;