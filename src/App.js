import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';

// Pages - Only import pages that exist and are being kept
import HomePage from './pages/HomePage';
import FindMarket from './pages/FindMarket';
import MarketDetail from './pages/MarketDetail';
import WeatherPage from './pages/WeatherPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProfilePage from './pages/ProfilePage';
import AboutPage from './pages/AboutPage';
import ContactPage from './pages/ContactPage';
import TermsPage from './pages/TermsPage';

// Layout Components
import Header from './components/common/Header';
import Footer from './components/common/Footer';

// Styles
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header />
          <main className="main-content">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<HomePage />} />
              <Route path="/find-market" element={<FindMarket />} />
              <Route path="/market/:id" element={<MarketDetail />} />
              <Route path="/weather" element={<WeatherPage />} />
              <Route path="/about" element={<AboutPage />} />
              <Route path="/contact" element={<ContactPage />} />
              
              {/* Auth Routes */}
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              
              {/* User Profile Routes */}
              <Route path="/profile" element={<ProfilePage />} />
              
              {/* Legal Pages */}
              <Route path="/terms" element={<TermsPage />} />
              
              {/* 404 Fallback */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;