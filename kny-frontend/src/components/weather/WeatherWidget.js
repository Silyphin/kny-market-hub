import React, { useState, useEffect } from 'react';
import { 
  Cloud, 
  Sun, 
  CloudRain, 
  CloudSnow, 
  CloudLightning, 
  CloudDrizzle,
  Wind,
  Droplets,
  Thermometer,
  RefreshCw,
  Calendar,
  TrendingUp,
  TrendingDown
} from 'lucide-react';

const WeatherWidget = ({ 
  weatherData = [], 
  onRefresh, 
  showForecast = true, 
  compact = false,
  className = ''
}) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [todayWeather, setTodayWeather] = useState(null);

  useEffect(() => {
    if (weatherData && weatherData.length > 0) {
      const today = new Date().toISOString().split('T')[0];
      const todayData = weatherData.find(w => w.forecastDate === today);
      setTodayWeather(todayData || weatherData[0]);
    }
  }, [weatherData]);

  // Get weather icon based on condition
  const getWeatherIcon = (condition, size = 'w-8 h-8') => {
    const iconMap = {
      'sunny': <Sun className={`${size} text-yellow-500`} />,
      'clear': <Sun className={`${size} text-yellow-500`} />,
      'cloudy': <Cloud className={`${size} text-gray-500`} />,
      'partly cloudy': <Cloud className={`${size} text-gray-400`} />,
      'overcast': <Cloud className={`${size} text-gray-600`} />,
      'rainy': <CloudRain className={`${size} text-blue-500`} />,
      'light rain': <CloudDrizzle className={`${size} text-blue-400`} />,
      'heavy rain': <CloudRain className={`${size} text-blue-600`} />,
      'thunderstorm': <CloudLightning className={`${size} text-purple-500`} />,
      'snow': <CloudSnow className={`${size} text-blue-200`} />,
    };
    
    const lowerCondition = condition?.toLowerCase() || '';
    
    // Find matching condition
    for (const [key, icon] of Object.entries(iconMap)) {
      if (lowerCondition.includes(key)) {
        return icon;
      }
    }
    
    // Default icon
    return <Cloud className={`${size} text-gray-500`} />;
  };

  // Get background gradient based on weather
  const getWeatherGradient = (condition) => {
    const lowerCondition = condition?.toLowerCase() || '';
    
    if (lowerCondition.includes('sunny') || lowerCondition.includes('clear')) {
      return 'bg-gradient-to-br from-yellow-400 via-orange-400 to-red-400';
    } else if (lowerCondition.includes('rain')) {
      return 'bg-gradient-to-br from-blue-400 via-blue-500 to-blue-600';
    } else if (lowerCondition.includes('cloud')) {
      return 'bg-gradient-to-br from-gray-400 via-gray-500 to-gray-600';
    } else if (lowerCondition.includes('thunder')) {
      return 'bg-gradient-to-br from-purple-400 via-purple-500 to-purple-600';
    }
    
    return 'bg-gradient-to-br from-blue-400 via-blue-500 to-blue-600';
  };

  // Format date
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    
    if (date.toDateString() === today.toDateString()) {
      return 'Today';
    } else if (date.toDateString() === tomorrow.toDateString()) {
      return 'Tomorrow';
    } else {
      return date.toLocaleDateString('en-MY', { 
        weekday: 'short', 
        month: 'short', 
        day: 'numeric' 
      });
    }
  };

  // Handle refresh
  const handleRefresh = async () => {
    if (onRefresh) {
      setIsLoading(true);
      try {
        await onRefresh();
      } finally {
        setIsLoading(false);
      }
    }
  };

  // Compact view for small spaces
  if (compact) {
    return (
      <div className={`bg-white rounded-lg shadow-md p-4 ${className}`}>
        {todayWeather ? (
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              {getWeatherIcon(todayWeather.weatherCondition, 'w-6 h-6')}
              <div>
                <div className="font-semibold text-lg">
                  {todayWeather.temperatureHigh}°C
                </div>
                <div className="text-sm text-gray-600">
                  {todayWeather.weatherCondition}
                </div>
              </div>
            </div>
            <div className="text-right">
              <div className="text-sm text-gray-600">
                Low: {todayWeather.temperatureLow}°C
              </div>
              <div className="text-sm text-blue-600">
                <Droplets className="inline w-3 h-3 mr-1" />
                {todayWeather.rainChance}%
              </div>
            </div>
          </div>
        ) : (
          <div className="text-center text-gray-500">
            No weather data available
          </div>
        )}
      </div>
    );
  }

  return (
    <div className={`bg-white rounded-lg shadow-lg overflow-hidden ${className}`}>
      {/* Current Weather Header */}
      {todayWeather && (
        <div className={`${getWeatherGradient(todayWeather.weatherCondition)} text-white p-6`}>
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              {getWeatherIcon(todayWeather.weatherCondition, 'w-16 h-16')}
              <div>
                <div className="text-3xl font-bold">
                  {todayWeather.temperatureHigh}°C
                </div>
                <div className="text-lg opacity-90">
                  {todayWeather.weatherCondition}
                </div>
                <div className="text-sm opacity-75">
                  Low: {todayWeather.temperatureLow}°C
                </div>
              </div>
            </div>
            
            <div className="text-right">
              <button
                onClick={handleRefresh}
                disabled={isLoading}
                className="p-2 rounded-full bg-white bg-opacity-20 hover:bg-opacity-30 transition-colors"
              >
                <RefreshCw className={`w-5 h-5 ${isLoading ? 'animate-spin' : ''}`} />
              </button>
            </div>
          </div>
          
          {/* Weather Details */}
          <div className="mt-4 grid grid-cols-2 gap-4">
            <div className="flex items-center space-x-2">
              <Droplets className="w-4 h-4" />
              <span className="text-sm">Rain: {todayWeather.rainChance}%</span>
            </div>
            <div className="flex items-center space-x-2">
              <Calendar className="w-4 h-4" />
              <span className="text-sm">{formatDate(todayWeather.forecastDate)}</span>
            </div>
          </div>
        </div>
      )}

      {/* Forecast */}
      {showForecast && weatherData && weatherData.length > 1 && (
        <div className="p-4">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
            <Calendar className="w-5 h-5 mr-2" />
            5-Day Forecast
          </h3>
          
          <div className="space-y-3">
            {weatherData.slice(0, 5).map((weather, index) => (
              <div
                key={weather.id || index}
                className={`flex items-center justify-between p-3 rounded-lg transition-colors cursor-pointer
                  ${selectedDate === weather.forecastDate ? 'bg-blue-50 border border-blue-200' : 'hover:bg-gray-50'}`}
                onClick={() => setSelectedDate(
                  selectedDate === weather.forecastDate ? null : weather.forecastDate
                )}
              >
                <div className="flex items-center space-x-3">
                  {getWeatherIcon(weather.weatherCondition, 'w-6 h-6')}
                  <div>
                    <div className="font-medium text-gray-900">
                      {formatDate(weather.forecastDate)}
                    </div>
                    <div className="text-sm text-gray-600">
                      {weather.weatherCondition}
                    </div>
                  </div>
                </div>
                
                <div className="flex items-center space-x-4">
                  <div className="text-right">
                    <div className="flex items-center space-x-1">
                      <TrendingUp className="w-3 h-3 text-red-500" />
                      <span className="font-semibold">{weather.temperatureHigh}°</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <TrendingDown className="w-3 h-3 text-blue-500" />
                      <span className="text-gray-600">{weather.temperatureLow}°</span>
                    </div>
                  </div>
                  
                  <div className="flex items-center space-x-1 text-blue-600">
                    <Droplets className="w-3 h-3" />
                    <span className="text-sm">{weather.rainChance}%</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* No Data State */}
      {(!weatherData || weatherData.length === 0) && (
        <div className="p-8 text-center">
          <Cloud className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-2">No Weather Data</h3>
          <p className="text-gray-600 mb-4">Weather information is not available at the moment.</p>
          {onRefresh && (
            <button
              onClick={handleRefresh}
              disabled={isLoading}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              <RefreshCw className={`w-4 h-4 mr-2 inline ${isLoading ? 'animate-spin' : ''}`} />
              Refresh Weather
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default WeatherWidget;