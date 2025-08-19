import React from 'react';
import { 
  MapPin, 
  Github, 
  Mail, 
  Heart, 
  ExternalLink,
  Cloud,
  Navigation
} from 'lucide-react';
import '../../styles/Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  const quickLinks = [
    { name: 'Home', href: '/' },
    { name: 'Find Market', href: '/find-market' },
    { name: 'Weather', href: '/weather' },
    { name: 'About', href: '/about' },
  ];

  const socialLinks = [
    { 
      name: 'GitHub', 
      href: 'https://github.com', 
      icon: Github
    },
    { 
      name: 'Email', 
      href: 'mailto:contact@knymarket.com', 
      icon: Mail
    },
  ];

  const features = [
    { name: 'Interactive Maps', icon: MapPin },
    { name: 'Weather Forecast', icon: Cloud },
    { name: 'Place Discovery', icon: Navigation },
  ];

  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          {/* Brand Section */}
          <div className="footer-brand">
            <div className="brand-header">
              <MapPin className="brand-icon" />
              <span className="brand-text">KNY Market</span>
            </div>
            <p className="brand-description">
              Discover amazing places in Penang with real-time weather information 
              and interactive maps. Your guide to exploring local markets, attractions, 
              and hidden gems.
            </p>
            
            {/* Features */}
            <div className="features-list">
              {features.map((feature) => (
                <div key={feature.name} className="feature-item">
                  <feature.icon className="feature-icon" />
                  <span className="feature-text">{feature.name}</span>
                </div>
              ))}
            </div>

            {/* Social Links */}
            <div className="social-links">
              {socialLinks.map((link) => (
                <a
                  key={link.name}
                  href={link.href}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="social-link"
                  aria-label={link.name}
                >
                  <link.icon className="social-icon" />
                </a>
              ))}
            </div>
          </div>

          {/* Quick Links */}
          <div className="links-section">
            <h3 className="links-title">Quick Links</h3>
            <ul className="links-list">
              {quickLinks.map((link) => (
                <li key={link.name}>
                  <a href={link.href} className="footer-link">
                    <span>{link.name}</span>
                    <ExternalLink className="link-icon" />
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* Support Links */}
          <div className="links-section">
            <h3 className="links-title">Support</h3>
            <ul className="links-list">
              <li>
                <a href="/contact" className="footer-link">Contact Us</a>
              </li>
              <li>
                <a href="/terms" className="footer-link">Terms of Service</a>
              </li>
            </ul>
          </div>
        </div>

        {/* Bottom Section */}
        <div className="footer-bottom">
          <div className="bottom-content">
            <div className="made-with-love">
              <span>Made with</span>
              <Heart className="heart-icon" />
              <span>in Malaysia</span>
            </div>
            
            <div className="copyright">
              © {currentYear} KNY Explorer. All rights reserved.
            </div>
          </div>
          
          {/* Technical Info */}
          <div className="tech-info">
            <p>
              Powered by Google Maps API • Weather data from OpenWeatherMap • 
              Built with React & Spring Boot
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;