package com.gcu.knapczyk_sandramaria_s2334978;

/*
 * Sandra Maria Knapczyk
 * Student ID: S2334978
 * MPD CW1
 */
public class Observations {
    private String day;          // The specific day of the week for the observation.
    private String date;         // The full date of the observation.
    private String temperature;  // The recorded temperature.
    private String pressure;     // The atmospheric pressure.
    private String windSpeed;    // The wind speed.

    // Getters and setters
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getPressure() { return pressure; }
    public void setPressure(String pressure) { this.pressure = pressure; }

    public String getWindSpeed() { return windSpeed; }
    public void setWindSpeed(String windSpeed) { this.windSpeed = windSpeed; }

}
