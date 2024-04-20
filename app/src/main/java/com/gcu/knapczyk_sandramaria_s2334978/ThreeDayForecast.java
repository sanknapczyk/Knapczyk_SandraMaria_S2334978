package com.gcu.knapczyk_sandramaria_s2334978;

/*
 * Sandra Maria Knapczyk
 * Student ID: S2334978
 * MPD CW1
 */
public class ThreeDayForecast {
    // Weather-related properties for the forecast
    private String locationName;          // Name of the location for the forecast
    private String title;                 // General title for the forecast
    private String description;           // Detailed description of weather conditions
    private String date;                  // Date of the forecast
    private String day;                   // Day of the week
    private String weatherCondition;      // Weather conditions such as Sunny, Cloudy, etc.
    private String maximumTemperature;    // Maximum temperature forecasted
    private String minimumTemperature;    // Minimum temperature forecasted
    private String windDirection;         // Direction of the wind
    private String windSpeed;             // Speed of the wind
    private String visibility;            // Visibility conditions
    private String pressure;              // Atmospheric pressure
    private String humidity;              // Humidity percentage
    private String uvRisk;                // UV radiation risk
    private String pollution;             // Pollution level
    private String sunset;                // Sunset time

    // Constructor
    public ThreeDayForecast() {
    }

    // Getter and Setter methods
    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }
    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }
    public void setMinimumTemperature(String minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getWindDirection() {
        return windDirection;
    }
    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getUvRisk() {
        return uvRisk;
    }
    public void setUvRisk(String uvRisk) {
        this.uvRisk = uvRisk;
    }

    public String getPollution() {
        return pollution;
    }
    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    public String getSunset() {
        return sunset;
    }
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
