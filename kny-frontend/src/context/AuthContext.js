// src/context/AuthContext.js
import React, { createContext, useContext, useReducer, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext();

// Auth state management
const authReducer = (state, action) => {
  switch (action.type) {
    case 'AUTH_LOADING':
      return { ...state, loading: true, error: null };
    case 'AUTH_SUCCESS':
      return { 
        ...state, 
        loading: false, 
        user: action.payload.user, 
        isAuthenticated: true,
        error: null 
      };
    case 'AUTH_ERROR':
      return { 
        ...state, 
        loading: false, 
        error: action.payload, 
        user: null, 
        isAuthenticated: false 
      };
    case 'AUTH_LOGOUT':
      return { 
        ...state, 
        user: null, 
        isAuthenticated: false, 
        loading: false, 
        error: null 
      };
    case 'CLEAR_ERROR':
      return { ...state, error: null };
    default:
      return state;
  }
};

const initialState = {
  user: null,
  isAuthenticated: false,
  loading: false,
  error: null
};

export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Check auth status on app load ONLY if not on login/register pages
  useEffect(() => {
    const currentPath = window.location.pathname;
    const publicPaths = ['/login', '/register'];
    
    if (!publicPaths.includes(currentPath)) {
      console.log('Checking auth status for protected page:', currentPath);
      checkAuthStatus();
    } else {
      console.log('Skipping auth check for public page:', currentPath);
    }
  }, []);

  const checkAuthStatus = async () => {
    try {
      dispatch({ type: 'AUTH_LOADING' });
      const response = await authService.getCurrentUser();
      
      console.log('Auth check raw response:', response); 
      

      let user;
      if (response.user) {
        // Regular email login response format: {user: {...}, message: "..."}
        user = response.user;
      } else if (response.email || response.name) {
        // OAuth login response format: {name: "...", email: "...", type: "oauth2"}
        user = response;
      } else {
        throw new Error('Invalid user response format');
      }
      
      console.log('Processed user:', user); 
      
      dispatch({ 
        type: 'AUTH_SUCCESS', 
        payload: { user } 
      });
    } catch (error) {
      console.log('Auth check failed:', error.message);
      dispatch({ 
        type: 'AUTH_ERROR', 
        payload: 'Not authenticated' 
      });
    }
  };

  const login = async (loginData) => {
    try {
      dispatch({ type: 'AUTH_LOADING' });
      const response = await authService.login(loginData);
      
      console.log('Login response:', response);
      
      // Extract user from response
      const user = response.user || response;
      
      dispatch({ 
        type: 'AUTH_SUCCESS', 
        payload: { user } 
      });
      
      return response;
    } catch (error) {
      console.error('Login error in context:', error);
      dispatch({ 
        type: 'AUTH_ERROR', 
        payload: error.message 
      });
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      dispatch({ type: 'AUTH_LOGOUT' });
    } catch (error) {
      console.error('Logout error:', error);
      // Force logout even if server call fails
      dispatch({ type: 'AUTH_LOGOUT' });
    }
  };

  const initiateOAuthLogin = () => {
    authService.initiateOAuthLogin();
  };

  const clearError = () => {
    dispatch({ type: 'CLEAR_ERROR' });
  };

  const value = {
    ...state,
    login,
    logout,
    initiateOAuthLogin,
    clearError,
    checkAuthStatus
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};