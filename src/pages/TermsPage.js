import React from 'react';
import '../styles/TermsPage.css';

const TermsPage = () => {
  return (
    <div className="terms-page">
      <div className="terms-container">
        <div className="terms-header">
          <h1>Terms and Conditions</h1>
          <p className="last-updated">Last updated: January 1, 2024</p>
        </div>

        <div className="terms-content">
          <section className="terms-section">
            <h2>1. Acceptance of Terms</h2>
            <p>
              By accessing and using this website, you accept and agree to be bound by the terms 
              and provision of this agreement. These Terms and Conditions govern your use of our 
              website and services.
            </p>
          </section>

          <section className="terms-section">
            <h2>2. Use License</h2>
            <p>
              Permission is granted to temporarily download one copy of the materials on our 
              website for personal, non-commercial transitory viewing only. This is the grant 
              of a license, not a transfer of title, and under this license you may not:
            </p>
            <ul>
              <li>modify or copy the materials</li>
              <li>use the materials for any commercial purpose or for any public display (commercial or non-commercial)</li>
              <li>attempt to decompile or reverse engineer any software contained on our website</li>
              <li>remove any copyright or other proprietary notations from the materials</li>
            </ul>
          </section>

          <section className="terms-section">
            <h2>3. Disclaimer</h2>
            <p>
              The materials on our website are provided on an 'as is' basis. We make no warranties, 
              expressed or implied, and hereby disclaim and negate all other warranties including 
              without limitation, implied warranties or conditions of merchantability, fitness for 
              a particular purpose, or non-infringement of intellectual property or other violation of rights.
            </p>
          </section>

          <section className="terms-section">
            <h2>4. Limitations</h2>
            <p>
              In no event shall our company or its suppliers be liable for any damages (including, 
              without limitation, damages for loss of data or profit, or due to business interruption) 
              arising out of the use or inability to use the materials on our website, even if we or 
              our authorized representative has been notified orally or in writing of the possibility 
              of such damage.
            </p>
          </section>

          <section className="terms-section">
            <h2>5. Accuracy of Materials</h2>
            <p>
              The materials appearing on our website could include technical, typographical, or 
              photographic errors. We do not warrant that any of the materials on its website are 
              accurate, complete, or current. We may make changes to the materials contained on its 
              website at any time without notice.
            </p>
          </section>

          <section className="terms-section">
            <h2>6. Contact Information</h2>
            <p>
              If you have any questions about these Terms and Conditions, please contact us at:
            </p>
            <div className="contact-info">
              <p>Email: legal@company.com</p>
              <p>Phone: +1 (555) 123-4567</p>
              <p>Address: 123 Business Street, Suite 100, City, State 12345</p>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
};

export default TermsPage;