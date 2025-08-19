# 🏪 KNY Market Hub

**Know Your Neighborhood Market Hub** - A comprehensive full-stack web application for discovering and exploring local markets in Penang, Malaysia with real-time weather integration and interactive mapping.

## 🌟 Features

### 🔍 Market Discovery
- **Interactive Market Search** - Find markets by name, specialty, or location
- **Location-Based Discovery** - Find nearby markets using GPS coordinates
- **Crowd Level Predictions** - Real-time crowd density information for optimal visit timing
- **Market Details** - Comprehensive information including hours, specialties, and facilities
- **Photo Galleries** - Market photos integrated with Google Places API

### 🌤️ Weather Integration
- **Real-Time Weather** - Current weather conditions for Penang
- **7-Day Forecast** - Extended weather forecasting for market visit planning
- **Weather-Based Tips** - Smart recommendations based on current conditions
- **Location-Specific Weather** - Weather data for specific market locations

### 🗺️ Interactive Maps
- **OpenStreetMap Integration** - Interactive maps with market locations
- **Directions** - Get directions to markets via Google Maps
- **Radius Search** - Find markets within specified distance
- **Marker Clustering** - Organized display of multiple market locations

### 👤 User Management
- **Multi-Authentication** - Email/password and OAuth2 (Google, Facebook)
- **User Profiles** - Customizable user profiles and preferences
- **Session Management** - Secure session handling with Spring Security
- **Protected Routes** - Role-based access control

### 📱 Modern UI/UX
- **Responsive Design** - Fully responsive across all devices
- **Progressive Web App** - PWA capabilities for mobile experience
- **Dark/Light Mode** - User preference-based theming
- **Accessibility** - WCAG compliant design

## 🏗️ Architecture

### Backend (Spring Boot)
```
src/main/java/com/kny/
├── config/                 # Configuration classes
│   ├── CorsConfig.java
│   ├── SecurityConfig.java
│   └── WeatherConfig.java
├── controller/             # REST controllers
│   ├── AuthController.java
│   ├── MarketController.java
│   ├── UserController.java
│   └── WeatherController.java
├── dto/                    # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── MarketResponse.java
│   └── UserResponse.java
├── model/                  # JPA entities
│   ├── Market.java
│   └── User.java
├── repository/             # Data access layer
│   ├── MarketRepository.java
│   └── UserRepository.java
├── service/                # Business logic
│   ├── AuthService.java
│   ├── MarketService.java
│   ├── UserService.java
│   └── WeatherService.java
└── KnyBackendApplication.java
```

### Frontend (React)
```
src/
├── components/             # Reusable components
│   ├── common/
│   │   ├── Header.js
│   │   ├── Footer.js
│   │   └── WeatherWidget.js
├── context/                # React Context
│   └── AuthContext.js
├── hooks/                  # Custom React hooks
│   ├── useApi.js
│   ├── useMarkets.js
│   └── useWeather.js
├── pages/                  # Page components
│   ├── HomePage.js
│   ├── FindMarket.js
│   ├── MarketDetail.js
│   ├── WeatherPage.js
│   ├── LoginPage.js
│   └── ProfilePage.js
├── services/               # API services
│   ├── api.js
│   ├── authService.js
│   ├── marketService.js
│   └── weatherService.js
├── styles/                 # CSS modules
└── App.js
```

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Security**: Spring Security with OAuth2
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **APIs**: Google Places API, OpenMeteo Weather API

### Frontend
- **Framework**: React 19.1.0
- **Routing**: React Router DOM 7.6.3
- **State Management**: Context API + Custom Hooks
- **Styling**: CSS Modules with responsive design
- **Maps**: Leaflet with OpenStreetMap
- **Icons**: Lucide React
- **Forms**: Formik with Yup validation

### Infrastructure
- **Database**: MySQL with HikariCP connection pooling
- **Session Management**: HTTP sessions with Redis (optional)
- **API Documentation**: Built-in Spring Boot Actuator
- **Monitoring**: Spring Boot Actuator endpoints

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Maven 3.8+**
- **Spring Tool Suite (STS) for Eclipse** - Backend development
- **Visual Studio Code** - Frontend development

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/kny-market-hub.git
cd kny-market-hub
```

### 2. Database Setup

#### Install and Configure MySQL
```sql
-- Create database
CREATE DATABASE kny_market_hub;

-- Import initial data
mysql -u root -p kny_market_hub < database/kny_market_hub.sql
```

### 3. Backend Setup (Spring Tool Suite for Eclipse)

#### Import Project to STS
1. **Open Spring Tool Suite (STS)**
2. **File** → **Import** → **Existing Maven Projects**
3. **Browse** to `kny-market-hub/kny-backend` folder
4. **Select** the project and click **Finish**

#### Configure Application Properties
1. **Navigate** to `src/main/resources/application.properties`
2. **Update** the following configurations:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/kny_market_hub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kuala_Lumpur
spring.datasource.username=root
spring.datasource.password=your_mysql_password

# Google OAuth2 (Get from Google Cloud Console)
spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret

# Facebook OAuth2 (Get from Facebook Developers)
spring.security.oauth2.client.registration.facebook.client-id=your_facebook_app_id
spring.security.oauth2.client.registration.facebook.client-secret=your_facebook_app_secret

# Google Maps API (Get from Google Cloud Console)
app.google.maps.api.key=your_google_maps_api_key
```

#### Run Backend in STS
1. **Right-click** on the project in Package Explorer
2. **Run As** → **Spring Boot App**
3. Or **Run As** → **Java Application** → Select `KnyBackendApplication`
4. **Alternative**: Right-click on `KnyBackendApplication.java` → **Run As** → **Java Application**

✅ **Backend will start on**: `http://localhost:8080`

#### Verify Backend is Running
- Open browser to `http://localhost:8080/api/markets/health`
- Should return: `{"status":"healthy","service":"market-service","timestamp":"..."}`

### 4. Frontend Setup (Visual Studio Code)

#### Open Project in VS Code
```bash
cd kny-frontend
code .  # Opens VS Code in current directory
```

#### Install Dependencies
**Open terminal in VS Code** (`Ctrl+Shift+`` ` or `View` → `Terminal`):
```bash
npm install
```

#### Configure Environment
**Create `.env` file** in `kny-frontend` root directory:
```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_GOOGLE_MAPS_API_KEY=your_google_maps_api_key
```

#### Run Frontend in VS Code
**In VS Code terminal**:
```bash
npm start
```

✅ **Frontend will start on**: `http://localhost:3000` (React's default port)

> **Note**: If port 3000 is busy, React will automatically prompt to use the next available port (usually 3001)

#### Verify Frontend is Running
- React will automatically open your browser to the correct port
- Manually check: `http://localhost:3000`
- Should display the KNY Market Hub homepage

### 5. Port Configuration Summary

Your application uses the following ports:
- **Backend (Spring Boot)**: `http://localhost:8080` ✅
- **Frontend (React)**: `http://localhost:3000` ✅ (default)

React automatically chooses port 3000 unless it's already in use. Your backend is correctly configured to accept requests from port 3000 via CORS settings.

### 6. Development Workflow

#### Backend Development (STS)
- **Auto-restart**: STS supports Spring Boot DevTools for automatic restart
- **Console Output**: Check the Console tab for logs and errors
- **Debug Mode**: Right-click project → **Debug As** → **Spring Boot App**
- **Database**: Use STS Data Source Explorer to connect to MySQL

#### Frontend Development (VS Code)
- **Hot Reload**: React automatically reloads on file changes
- **Console**: Use browser DevTools (F12) for debugging
- **Terminal**: Use integrated terminal for npm commands
- **Extensions**: Utilize React and JavaScript extensions for better development experience

### 6. Common Development Commands

#### Backend (STS Terminal or External Terminal)
```bash
cd kny-backend

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn clean package

# Run with different profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Frontend (VS Code Terminal)
```bash
cd kny-frontend

# Start development server
npm start

# Run tests
npm test

# Build for production
npm run build

# Install new package
npm install package-name

# Check for vulnerabilities
npm audit
```

### 7. Troubleshooting

#### Backend Issues
- **Port 8080 in use**: Change port in `application.properties`: `server.port=8081`
- **Database connection failed**: Verify MySQL is running and credentials are correct
- **Maven dependencies**: Right-click project → **Maven** → **Reload Projects**

#### Frontend Issues
- **Port 3000 in use**: React will prompt to use different port (usually 3001)
- **API calls failing**: Verify backend is running on `http://localhost:8080`
- **Module not found**: Delete `node_modules` and `package-lock.json`, then run `npm install`

### 8. IDE-Specific Tips

#### Spring Tool Suite (STS)
- **Spring Boot Dashboard**: Use to manage multiple Spring Boot applications
- **Application Properties Editor**: Built-in editor with auto-completion
- **Live Beans Graph**: Visualize Spring application context
- **Request Mappings**: View all available endpoints

#### Visual Studio Code
- **Integrated Terminal**: Use `Ctrl+Shift+`` ` for quick terminal access
- **File Explorer**: Use `Ctrl+Shift+E` to navigate project files
- **Search**: Use `Ctrl+Shift+F` for project-wide search
- **Git Integration**: Built-in Git support for version control

## 📡 API Documentation

### Authentication Endpoints
```http
POST   /api/auth/register     # User registration
POST   /api/auth/login        # User login
GET    /api/auth/user         # Get current user
POST   /api/auth/logout       # User logout
```

### Market Endpoints
```http
GET    /api/markets                    # Get all markets
GET    /api/markets/{id}               # Get market by ID
GET    /api/markets/search             # Search markets
GET    /api/markets/nearby             # Find nearby markets
GET    /api/markets/covered            # Get covered markets
GET    /api/markets/crowd-level        # Filter by crowd level
POST   /api/markets/{id}/sync          # Sync with Google Places
GET    /api/markets/statistics         # Market statistics
```

### Weather Endpoints
```http
GET    /weather/api                    # Current weather
GET    /weather/api/location           # Weather for coordinates
```

### Example API Calls

#### Get Nearby Markets
```bash
curl -X GET "http://localhost:8080/api/markets/nearby?latitude=5.4164&longitude=100.3327&radius=10.0" \
  -H "Content-Type: application/json"
```

#### Search Markets
```bash
curl -X GET "http://localhost:8080/api/markets/search?name=Pulau&specialty=seafood" \
  -H "Content-Type: application/json"
```

#### User Login
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password123"}'
```

## 🗄️ Database Schema

### Markets Table
```sql
CREATE TABLE markets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    google_place_id VARCHAR(255) UNIQUE,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    description TEXT,
    specialties TEXT,
    highlights TEXT,
    is_covered BOOLEAN DEFAULT FALSE,
    crowd_level_morning ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    crowd_level_afternoon ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    crowd_level_evening ENUM('LOW','MEDIUM','HIGH') DEFAULT 'LOW',
    data_source ENUM('LOCAL','GOOGLE','HYBRID') DEFAULT 'HYBRID',
    phone_number VARCHAR(20),
    website VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    provider VARCHAR(20) DEFAULT 'email',
    provider_id VARCHAR(255),
    oauth_id VARCHAR(255),
    oauth_provider VARCHAR(255),
    phone_number VARCHAR(255),
    profile_picture VARCHAR(255),
    is_active BIT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🔧 Configuration

### OAuth2 Setup

#### Google OAuth2
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth2 credentials
5. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`

#### Facebook OAuth2
1. Go to [Facebook Developers](https://developers.facebook.com/)
2. Create a new app
3. Add Facebook Login product
4. Add redirect URI: `http://localhost:8080/login/oauth2/code/facebook`

### Google Maps API
1. Enable Maps JavaScript API and Places API
2. Create API key with restrictions
3. Add to application.properties

## 🔒 Security Features

- **CORS Configuration** - Properly configured for cross-origin requests
- **CSRF Protection** - Disabled for API endpoints, enabled for form submissions
- **Session Management** - Secure session handling with configurable timeout
- **Password Encryption** - BCrypt password hashing
- **OAuth2 Integration** - Secure third-party authentication
- **Input Validation** - Comprehensive validation with Bean Validation

## 🎨 Frontend Features

### Responsive Design
- **Mobile-First** - Optimized for mobile devices
- **Tablet Support** - Adapted layouts for tablets
- **Desktop Experience** - Full-featured desktop interface

### Performance
- **Code Splitting** - Lazy loading of components
- **Optimized Images** - Responsive image loading
- **Caching** - API response caching with React Query
- **Minification** - Production build optimization

## 🧪 Testing

### Backend Testing
```bash
cd kny-backend
mvn test                    # Run unit tests
mvn integration-test        # Run integration tests
mvn test jacoco:report      # Generate coverage report
```

### Frontend Testing
```bash
cd kny-frontend
npm test                    # Run unit tests
npm run test:coverage       # Generate coverage report
npm run test:e2e           # Run end-to-end tests
```

## 📦 Deployment

### Production Build

#### Backend
```bash
mvn clean package -Pprod
java -jar target/kny-backend-0.0.1-SNAPSHOT.jar
```

#### Frontend
```bash
npm run build
# Deploy build/ folder to web server
```


### Environment Variables (Production)
```env
# Backend
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://prod-db:3306/kny_market_hub
GOOGLE_CLIENT_ID=prod_google_client_id
FACEBOOK_CLIENT_ID=prod_facebook_client_id
GOOGLE_MAPS_API_KEY=prod_google_maps_key

# Frontend
REACT_APP_API_URL=https://api.knymarkethub.com
REACT_APP_GOOGLE_MAPS_API_KEY=prod_google_maps_key
```

### Development Guidelines
- Follow Java coding standards and Spring Boot best practices
- Use ESLint and Prettier for frontend code formatting
- Write unit tests for new features
- Update documentation for API changes
- Follow semantic versioning for releases

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **OpenMeteo** for free weather API
- **OpenStreetMap** for map tiles
- **Google Places API** for market data enrichment
- **Spring Boot** community for excellent documentation
- **React** community for comprehensive ecosystem

