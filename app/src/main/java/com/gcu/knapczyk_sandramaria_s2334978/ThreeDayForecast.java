package com.gcu.knapczyk_sandramaria_s2334978;

public class ThreeDayForecast {
    private String locationName;
    private String title;
    private String description;
    private String date;
    private String day;
    private String weatherCondition;
    private String maximumTemperature;
    private String minimumTemperature;
    private String windDirection;
    private String windSpeed;
    private String visibility;
    private String pressure;
    private String humidity;
    private String uvRisk;
    private String pollution;
    private String sunset;

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

    // Add toString method to easily print the details of the forecast
    @Override
    public String toString() {
        return "ThreeDayForecast{" +
                "title='" + safeString(title) + '\'' +
                ", day='" + safeString(day) + '\'' +
                ", weatherCondition='" + safeString(weatherCondition) + '\'' +
                ", description='" + safeString(description) + '\'' +
                ", date='" + safeString(date) + '\'' +
                ", maxTemp='" + safeString(maximumTemperature) + '\'' +
                ", minTemp='" + safeString(minimumTemperature) + '\'' +
                ", windDirection='" + safeString(windDirection) + '\'' +
                ", windSpeed='" + safeString(windSpeed) + '\'' +
                ", visibility='" + safeString(visibility) + '\'' +
                ", pressure='" + safeString(pressure) + '\'' +
                ", humidity='" + safeString(humidity) + '\'' +
                ", uvRisk='" + safeString(uvRisk) + '\'' +
                ", pollution='" + safeString(pollution) + '\'' +
                ", sunset='" + safeString(sunset) + '\'' +
                '}';
    }

    private String safeString(String input) {
        return input != null ? input : "N/A";
    }

}
