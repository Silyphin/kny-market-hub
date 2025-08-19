import React from 'react';
import '../styles/AboutPage.css';

const AboutPage = () => {
  return (
    <div className="about-page">
      <div className="about-container">
        <div className="about-header">
          <h1>About Us</h1>
          <p className="about-subtitle">Discover our story and mission</p>
        </div>
        
        <div className="about-content">
          <section className="about-section">
            <h2>Our Story</h2>
            <p>
              Founded with a vision to revolutionize the way people interact with technology, 
              our company has been at the forefront of innovation since our inception. We believe 
              in creating solutions that not only meet current needs but also anticipate future challenges.
            </p>
          </section>

          <section className="about-section">
            <h2>Our Mission</h2>
            <p>
              To empower individuals and businesses through cutting-edge technology solutions 
              that enhance productivity, connectivity, and overall quality of life. We strive 
              to make complex technologies accessible and user-friendly for everyone.
            </p>
          </section>

          <section className="about-section">
            <h2>Our Values</h2>
            <div className="values-grid">
              <div className="value-item">
                <h3>Innovation</h3>
                <p>Constantly pushing boundaries to create groundbreaking solutions</p>
              </div>
              <div className="value-item">
                <h3>Quality</h3>
                <p>Delivering excellence in every product and service we provide</p>
              </div>
              <div className="value-item">
                <h3>Integrity</h3>
                <p>Building trust through transparency and ethical business practices</p>
              </div>
              <div className="value-item">
                <h3>Customer Focus</h3>
                <p>Putting our customers' needs at the center of everything we do</p>
              </div>
            </div>
          </section>

          <section className="about-section">
            <h2>Our Team</h2>
            <p>
              Our diverse team of experts brings together years of experience in technology, 
              design, and business strategy. We're passionate about what we do and committed 
              to helping our clients achieve their goals.
            </p>
          </section>
        </div>
      </div>
    </div>
  );
};

export default AboutPage;