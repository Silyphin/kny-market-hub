// src/components/common/Header.js
import React, { useState, useEffect, useRef } from 'react';
import { 
  MapPin, 
  Menu, 
  X, 
  User, 
  LogOut, 
  Settings,
  Home,
  Navigation,
  Cloud,
  Info,
  UserPlus,
  UserCircle
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import '../../styles/Header.css';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const { user, isAuthenticated, logout } = useAuth();
  const userMenuRef = useRef(null);

  // DEBUG: Add console logs to see what's happening
  console.log('Header render - isAuthenticated:', isAuthenticated);
  console.log('Header render - user:', user);

  const navigationItems = [
    { name: 'Home', href: '/', icon: Home },
    { name: 'Find Market', href: '/find-market', icon: Navigation },
    { name: 'Weather', href: '/weather', icon: Cloud },
    { name: 'About', href: '/about', icon: Info },
  ];

  // Close user menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target)) {
        setIsUserMenuOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleLogout = async () => {
    try {
      await logout();
      setIsUserMenuOpen(false);
      window.location.href = '/'; // Redirect to home after logout
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  // FIXED: Handle both OAuth and email users
  const getUserDisplayName = () => {
    if (user?.name) return user.name;
    if (user?.email) return user.email.split('@')[0];
    return 'User';
  };

  const getUserEmail = () => {
    return user?.email || 'No email';
  };

  const getUserInitials = () => {
    const name = getUserDisplayName();
    return name.split(' ').map(n => n[0]).join('').toUpperCase();
  };

  const getUserAvatar = () => {
    // OAuth users might have 'picture' field, email users have 'profilePicture'
    return user?.picture || user?.profilePicture;
  };

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-content">
          {/* Logo */}
          <div className="logo-section">
            <a href="/" className="logo-link">
              <MapPin className="logo-icon" />
              <span className="logo-text">KNY Market</span>
            </a>
          </div>

          {/* Desktop Navigation */}
          <nav className="desktop-nav">
            {navigationItems.map((item) => (
              <a key={item.name} href={item.href} className="nav-item">
                <item.icon className="nav-icon" />
                {item.name}
              </a>
            ))}
          </nav>

          {/* User Actions */}
          <div className="user-section">
            {isAuthenticated ? (
              <div className="user-menu" ref={userMenuRef}>
                <button
                  onClick={() => setIsUserMenuOpen(!isUserMenuOpen)}
                  className="user-button authenticated"
                >
                  <div className="user-avatar">
                    {getUserAvatar() ? (
                      <img 
                        src={getUserAvatar()} 
                        alt="Profile" 
                        className="avatar-image"
                      />
                    ) : (
                      <div className="avatar-initials">
                        {getUserInitials()}
                      </div>
                    )}
                  </div>
                  <span className="user-name">{getUserDisplayName()}</span>
                </button>

                {/* User Dropdown */}
                {isUserMenuOpen && (
                  <div className="user-dropdown">
                    <div className="user-info">
                      <p className="user-name-dropdown">{getUserDisplayName()}</p>
                      <p className="user-email">{getUserEmail()}</p>
                    </div>
                    <div className="dropdown-divider"></div>
                    <a href="/profile" className="dropdown-item">
                      <UserCircle className="dropdown-icon" />
                      View Profile
                    </a>
                    <div className="dropdown-divider"></div>
                    <button onClick={handleLogout} className="dropdown-item logout-item">
                      <LogOut className="dropdown-icon" />
                      Sign Out
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <div className="auth-buttons">
                <a href="/login" className="signin-btn">Sign In</a>
                <a href="/register" className="signup-btn">
                  <UserPlus className="signup-icon" />
                  Sign Up
                </a>
              </div>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="mobile-menu-button">
            <button onClick={() => setIsMenuOpen(!isMenuOpen)} className="menu-btn">
              {isMenuOpen ? <X className="menu-icon" /> : <Menu className="menu-icon" />}
            </button>
          </div>
        </div>

        {/* Mobile Menu */}
        {isMenuOpen && (
          <div className="mobile-menu">
            <div className="mobile-nav">
              {navigationItems.map((item) => (
                <a
                  key={item.name}
                  href={item.href}
                  className="mobile-nav-item"
                  onClick={() => setIsMenuOpen(false)}
                >
                  <item.icon className="mobile-nav-icon" />
                  {item.name}
                </a>
              ))}
              
              {/* Mobile Auth Actions */}
              <div className="mobile-auth">
                {isAuthenticated ? (
                  <div className="mobile-user-section">
                    <div className="mobile-user-info">
                      <div className="mobile-user-avatar">
                        {getUserAvatar() ? (
                          <img 
                            src={getUserAvatar()} 
                            alt="Profile" 
                            className="mobile-avatar-image"
                          />
                        ) : (
                          <div className="mobile-avatar-initials">
                            {getUserInitials()}
                          </div>
                        )}
                      </div>
                      <div>
                        <p className="mobile-user-name">{getUserDisplayName()}</p>
                        <p className="mobile-user-email">{getUserEmail()}</p>
                      </div>
                    </div>
                    <a 
                      href="/profile" 
                      className="mobile-nav-item"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <UserCircle className="mobile-nav-icon" />
                      Profile
                    </a>
                    <button onClick={handleLogout} className="mobile-nav-item mobile-logout">
                      <LogOut className="mobile-nav-icon" />
                      Sign Out
                    </button>
                  </div>
                ) : (
                  <div className="mobile-auth-buttons">
                    <a 
                      href="/login" 
                      className="mobile-signin"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      Sign In
                    </a>
                    <a 
                      href="/register" 
                      className="mobile-signup"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      <UserPlus className="mobile-signup-icon" />
                      Sign Up
                    </a>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;