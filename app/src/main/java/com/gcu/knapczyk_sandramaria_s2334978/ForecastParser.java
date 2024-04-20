package com.gcu.knapczyk_sandramaria_s2334978;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Sandra Maria Knapczyk
 * Student ID: S2334978
 * MPD CW1
 */
public class ForecastParser {
    /**
     * Fetches and parses forecast data from a given URL.
     * @param urlString The URL to fetch forecast XML data from.
     * @return A list of ThreeDayForecast objects containing parsed forecast data.
     */
    public static CopyOnWriteArrayList<ThreeDayForecast> fetchDataAndParse(String urlString) {
        CopyOnWriteArrayList<ThreeDayForecast> currentForecasts = new CopyOnWriteArrayList<>();
        try {
            URL url = new URL(urlString);
            URLConnection yc = url.openConnection();
            try (InputStream in = yc.getInputStream()) {
                // Parse the input stream
                // Assuming parseDataF is a method that parses the input stream into a list of ThreeDayForecast
                parseDataF(in, currentForecasts);
            }
        } catch (IOException e) {
            Log.e("MyTag", "IOException in fetchDataAndParse", e);
        }
        return currentForecasts;
    }

    /**
     * Parses the XML input stream and populates a list of forecast data.
     * @param in The input stream from which to read the XML data.
     * @param currentForecasts The list to populate with parsed forecast data.
     */
    private static void parseDataF(InputStream in, CopyOnWriteArrayList<ThreeDayForecast> currentForecasts) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);
            int eventType = xpp.getEventType();
            ThreeDayForecast newForecast = null;
            String locationName = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("channel")) {
                        // Parse the title to get the location name
                        int nextEvent = xpp.next();
                        if (nextEvent == XmlPullParser.TEXT) {
                            String title = xpp.getText();
                            locationName = title.replace("BBC Weather - Forecast for", "").trim();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("item")) {
                        newForecast = new ThreeDayForecast();
                        newForecast.setLocationName(locationName);
                    } else if (newForecast != null) {
                        if (xpp.getName().equalsIgnoreCase("title")) {
                            String title = xpp.nextText();
                            Log.d("MyTag", "Parsing title: " + title); // Logging the title for debugging

                            // Extracting day and condition
                            String[] parts = title.split(",", 2);
                            if (parts.length > 0) {
                                String[] titleParts = parts[0].split(":", 2);
                                if (titleParts.length == 2) {
                                    newForecast.setDay(titleParts[0].trim());
                                    newForecast.setWeatherCondition(titleParts[1].trim());
                                }
                            }

                            // Initializing default values
                            String minTemp = "N/A";
                            String maxTemp = "N/A";

                            // Attempt to parse temperatures
                            if (title.contains("Minimum Temperature")) {
                                try {
                                    String[] tempParts = title.split("Minimum Temperature:")[1].split("Â°C")[0].trim().split(" ");
                                    minTemp = tempParts[0];
                                } catch (Exception e) {
                                    Log.e("MyTag", "Error parsing minimum temperature", e);
                                }
                            }

                            if (title.contains("Maximum Temperature")) {
                                try {
                                    String[] tempParts = title.split("Maximum Temperature:")[1].split("Â°C")[0].trim().split(" ");
                                    maxTemp = tempParts[0];
                                } catch (Exception e) {
                                    Log.e("MyTag", "Error parsing maximum temperature", e);
                                }
                            }

                            // Setting the temperatures
                            newForecast.setMinimumTemperature(minTemp);
                            newForecast.setMaximumTemperature(maxTemp);
                            Log.d("MyTag", "Parsed temperatures - Min: " + minTemp + ", Max: " + maxTemp);

                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            String description = xpp.nextText();
                            newForecast.setDescription(description);
                            // Parse the description for various pieces of information
                            parseDescription(description, newForecast);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            newForecast.setDate(xpp.nextText());
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item") && newForecast != null) {
                    currentForecasts.add(newForecast);
                    newForecast = null;
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("MyTag", "Error during parsing", e);
        }
        Log.d("MyTag", "End of document reached. Forecasts size: " + currentForecasts.size());
    }

    /**
     * Parses the description text from a weather forecast XML feed to extract detailed weather conditions
     * and set them into the provided ThreeDayForecast object.
     *
     * @param description The string containing all weather condition data.
     * @param forecast The ThreeDayForecast object to populate with parsed data.
     */
    private static void parseDescription(String description, ThreeDayForecast forecast) {

        // Extract wind direction using a regex pattern and set it on the forecast object
        Pattern windDirPattern = Pattern.compile("Wind Direction: (\\w+ \\w+)");
        Matcher windDirMatcher = windDirPattern.matcher(description);
        if (windDirMatcher.find()) {
            forecast.setWindDirection(windDirMatcher.group(1));
            Log.d("WeatherParse", "Parsed Wind Direction: " + windDirMatcher.group(1));
        }

        // Extract wind speed using a regex pattern and set it on the forecast object
        Pattern windSpeedPattern = Pattern.compile("Wind Speed: (\\d+)mph");
        Matcher windSpeedMatcher = windSpeedPattern.matcher(description);
        if (windSpeedMatcher.find()) {
            forecast.setWindSpeed(windSpeedMatcher.group(1) + "mph");
            Log.d("WeatherParse", "Parsed Wind Speed: " + windSpeedMatcher.group(1) + "mph");
        }

        // Extract visibility using a regex pattern and set it on the forecast object
        Pattern visibilityPattern = Pattern.compile("Visibility: (\\w+)");
        Matcher visibilityMatcher = visibilityPattern.matcher(description);
        if (visibilityMatcher.find()) {
            forecast.setVisibility(visibilityMatcher.group(1));
            Log.d("WeatherParse", "Parsed Visibility: " + visibilityMatcher.group(1));
        }

        // Extract atmospheric pressure using a regex pattern and set it on the forecast object
        Pattern pressurePattern = Pattern.compile("Pressure: (\\d+)mb");
        Matcher pressureMatcher = pressurePattern.matcher(description);
        if (pressureMatcher.find()) {
            forecast.setPressure(pressureMatcher.group(1) + "mb");
            Log.d("WeatherParse", "Parsed Pressure: " + pressureMatcher.group(1) + "mb");
        }

        // Extract humidity level using a regex pattern and set it on the forecast object
        Pattern humidityPattern = Pattern.compile("Humidity: (\\d+)%");
        Matcher humidityMatcher = humidityPattern.matcher(description);
        if (humidityMatcher.find()) {
            forecast.setHumidity(humidityMatcher.group(1) + "%");
            Log.d("WeatherParse", "Parsed Humidity: " + humidityMatcher.group(1) + "%");
        }

        // Extract UV risk level using a regex pattern and set it on the forecast object
        Pattern uvRiskPattern = Pattern.compile("UV Risk: (\\d+)");
        Matcher uvRiskMatcher = uvRiskPattern.matcher(description);
        if (uvRiskMatcher.find()) {
            forecast.setUvRisk(uvRiskMatcher.group(1));
            Log.d("WeatherParse", "Parsed UV Risk: " + uvRiskMatcher.group(1));
        }

        // Extract pollution level using a regex pattern and set it on the forecast object
        Pattern pollutionPattern = Pattern.compile("Pollution: (\\w+)");
        Matcher pollutionMatcher = pollutionPattern.matcher(description);
        if (pollutionMatcher.find()) {
            forecast.setPollution(pollutionMatcher.group(1));
            Log.d("WeatherParse", "Parsed Pollution: " + pollutionMatcher.group(1));
        }

        // Extract sunset time using a regex pattern and set it on the forecast object
        Pattern sunsetPattern = Pattern.compile("Sunset: (\\d{2}:\\d{2} [A-Z]+)");
        Matcher sunsetMatcher = sunsetPattern.matcher(description);
        if (sunsetMatcher.find()) {
            forecast.setSunset(sunsetMatcher.group(1));
            Log.d("WeatherParse", "Parsed Sunset: " + sunsetMatcher.group(1));
        }
    }

}
