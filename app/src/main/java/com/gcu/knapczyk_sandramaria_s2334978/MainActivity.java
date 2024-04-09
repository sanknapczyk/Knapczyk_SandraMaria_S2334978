package com.gcu.knapczyk_sandramaria_s2334978;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.ViewFlipper;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{
    private GoogleMap googleMap;
    private ScrollView scrollView;
    private ViewSwitcher viewSwitcher;
    private ViewFlipper weatherViewFlipper1, weatherViewFlipper2;
    private Button backButton, forButton;
    private ImageButton nextButton1, prevButton1, nextButton2, prevButton2;
    private Spinner locationSpinner;
    private TextView glasgowFor, londonFor, newyorkFor, omanFor, mauritiusFor, bangladeshFor;
    // Glasgow
    private TextView glasgowDay, glasgowTemperature, glasgowPressure, glasgowWind;

    // London
    private TextView londonDay, londonTemperature, londonPressure, londonWind;

    // New York
    private TextView newYorkDay, newYorkTemperature, newYorkPressure, newYorkWind;

    // Oman
    private TextView omanDay, omanTemperature, omanPressure, omanWind;

    // Mauritius
    private TextView mauritiusDay, mauritiusTemperature, mauritiusPressure, mauritiusWind;

    // Bangladesh
    private TextView bangladeshDay, bangladeshTemperature, bangladeshPressure, bangladeshWind;


    private final LatLng[] CITY_COORDINATES = new LatLng[]{
            new LatLng(55.8642, -4.2518), // Glasgow
            new LatLng(51.5074, -0.1278), // London
            new LatLng(40.7128, -74.0060), // New York
            new LatLng(20.4883, 56.2402), // Oman
            new LatLng(-20.4047, 57.4117), // Mauritius
            new LatLng(24.2079, 90.2569)  // Bangladesh
    };

    private String[] observationUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"
    };

    private String[] forecastUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    CopyOnWriteArrayList<ThreeDayForecast> forecasts = new CopyOnWriteArrayList<>();
    private Map<String, CopyOnWriteArrayList<Observations>> observationsMap = new ConcurrentHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews for 6 locations
        glasgowDay = findViewById(R.id.glasgowDay);
        glasgowTemperature = findViewById(R.id.glasgowTemperature);
        glasgowPressure = findViewById(R.id.glasgowPressure);
        glasgowWind = findViewById(R.id.glasgowWind);

        londonDay = findViewById(R.id.londonDay);
        londonTemperature = findViewById(R.id.londonTemperature);
        londonPressure = findViewById(R.id.londonPressure);
        londonWind = findViewById(R.id.londonWind);

        newYorkDay = findViewById(R.id.newYorkDay);
        newYorkTemperature = findViewById(R.id.newYorkTemperature);
        newYorkPressure = findViewById(R.id.newYorkPressure);
        newYorkWind = findViewById(R.id.newYorkWind);

        omanDay = findViewById(R.id.omanDay);
        omanTemperature = findViewById(R.id.omanTemperature);
        omanPressure = findViewById(R.id.omanPressure);
        omanWind = findViewById(R.id.omanWind);

        mauritiusDay = findViewById(R.id.mauritiusDay);
        mauritiusTemperature = findViewById(R.id.mauritiusTemperature);
        mauritiusPressure = findViewById(R.id.mauritiusPressure);
        mauritiusWind = findViewById(R.id.mauritiusWind);

        bangladeshDay = findViewById(R.id.bangladeshDay);
        bangladeshTemperature = findViewById(R.id.bangladeshTemperature);
        bangladeshPressure = findViewById(R.id.bangladeshPressure);
        bangladeshWind = findViewById(R.id.bangladeshWind);

        glasgowFor = (TextView) findViewById(R.id.glasgowFor);
        londonFor = (TextView) findViewById(R.id.londonFor);
        newyorkFor = (TextView) findViewById(R.id.newyorkFor);
        omanFor = (TextView) findViewById(R.id.omanFor);
        mauritiusFor = (TextView) findViewById(R.id.mauritiusFor);
        bangladeshFor = (TextView) findViewById(R.id.bangladeshFor);

        // Set up link to layout views
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        viewSwitcher = findViewById(R.id.viewSwitcher);
        if (viewSwitcher == null) {
            Toast.makeText(getApplicationContext(), "Null ViewSwitcher",
                    Toast.LENGTH_LONG);
            Log.e(getPackageName(), "null pointer");
        }

        // Initialise ViewFlippers
        weatherViewFlipper1 = findViewById(R.id.weatherViewFlipper1);
        weatherViewFlipper2 = findViewById(R.id.weatherFlipper2);

        //Initialise Spinner
        locationSpinner = findViewById(R.id.location_spinner);

        // Populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array, R.layout.spinner_item_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        // Set the spinner's item selected listener
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Set ViewFlipper to show chosen location
                weatherViewFlipper1.setDisplayedChild(position);
                // Update the map location
                updateMapLocation(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Display the first child and map view by default
                weatherViewFlipper1.setDisplayedChild(0);
                updateMapLocation(0);
            }
        });


        // Set up link to buttons
        forButton = findViewById(R.id.forButton);
        nextButton1 = findViewById(R.id.buttonNext1);
        prevButton1 = findViewById(R.id.buttonPrevious1);
        nextButton2 = findViewById(R.id.buttonNext2);
        prevButton2 = findViewById(R.id.buttonPrevious2);
        backButton = findViewById(R.id.backButton);
        forButton.setOnClickListener(this);
        nextButton1.setOnClickListener(this);
        prevButton1.setOnClickListener(this);
        nextButton2.setOnClickListener(this);
        prevButton2.setOnClickListener(this);
        backButton.setOnClickListener(this);
        startProgressObservations();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // to the OnMapReadyCallback
        }
    }



    @Override
    public void onClick(View v) {
        if (v == forButton) {
            viewSwitcher.showNext();
            startProgressForecasts();
            Log.d("MyTag", "go to forecast screen");
        } else if (v == nextButton1) {
            int nextPosition = (weatherViewFlipper1.getDisplayedChild() + 1) % weatherViewFlipper1.getChildCount();
            weatherViewFlipper1.setDisplayedChild(nextPosition);
            locationSpinner.setSelection(nextPosition);
            updateMapLocation(nextPosition);
            Log.d("MyTag", "obs next screen");
        } else if (v == prevButton1) {
            int prevPosition = (weatherViewFlipper1.getDisplayedChild() - 1 + weatherViewFlipper1.getChildCount()) % weatherViewFlipper1.getChildCount();
            weatherViewFlipper1.setDisplayedChild(prevPosition);
            locationSpinner.setSelection(prevPosition);
            updateMapLocation(prevPosition);
            Log.d("MyTag", "obs prev screen");
        } else if (v == nextButton2) {
            weatherViewFlipper2.showNext();
            Log.d("MyTag", "forecast next screen");
        } else if (v == prevButton2) {
            weatherViewFlipper2.showPrevious();
            Log.d("MyTag", "forecast prev screen");
        } else if (v == backButton) {
            viewSwitcher.showPrevious();
            startProgressObservations();
            Log.d("MyTag", "back to start screen");
        }
    }

        // Start the task with both URLs
    public void startProgressObservations() {
        Log.d("MyTag", "Starting observations progress");
        new Thread(new Task2(observationUrls)).start();
    }


    public void startProgressForecasts() {
        Log.d("MyTag", "Starting forecasts progress");
            new Thread(new Task(forecastUrls)).start();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMapLocation(0);
    }

    private void updateMapLocation(int position) {
        if (googleMap == null || position < 0 || position >= CITY_COORDINATES.length) {
            Log.e("MainActivity", "Invalid map state or position.");
            return;
        }

        LatLng selectedLocation = CITY_COORDINATES[position];
        googleMap.clear(); // Clear existing markers if any
        googleMap.addMarker(new MarkerOptions().position(selectedLocation).title("Marker in " + locationSpinner.getSelectedItem().toString()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10)); // Zoom can be adjusted as needed
    }


    private class Task2 implements Runnable {
        private String[] urls;

        public Task2(String[] urls) {
            this.urls = urls;
        }

        @Override
        public void run() {
            Log.d("MyTag", "Task2 started");

            for (String url : urls) {
                // Create a new list for the current URL's observations
                CopyOnWriteArrayList<Observations> currentObservations = new CopyOnWriteArrayList<>();

                // Fetch and process data into the current list
                fetchDataAndParse(url, currentObservations);

                // Store the list in the map, keyed by URL
                observationsMap.put(url, currentObservations);

                // Use the current list to update the UI
                runOnUiThread(() -> updateLocationViews(currentObservations, url));
            }
        }

        private void fetchDataAndParse(String urlString, CopyOnWriteArrayList<Observations> currentObservations) {
            try {
                URL url = new URL(urlString);
                URLConnection yc = url.openConnection();
                try (InputStream in = yc.getInputStream()) {
                    parseDataO(in, currentObservations); // Parse the input stream
                }
            } catch (IOException e) {
                Log.e("MyTag", "IOException in fetchDataAndParse", e);
            }
        }


        private void updateLocationViews(CopyOnWriteArrayList<Observations> observations, String url) {
            // As each URL corresponds to a single observation
            if (!observations.isEmpty()) {
                Observations observation = observations.get(0);

                // assigning parsed elements to text views in locations
                if (url.equals(observationUrls[0])) { // Glasgow's URL
                    updateViewsForLocation(observation, glasgowDay, glasgowTemperature, glasgowPressure, glasgowWind);
                    Log.d("LocationData", "Updating UI for Glasgow");
                } else if (url.equals(observationUrls[1])) { // London's URL
                    updateViewsForLocation(observation, londonDay, londonTemperature, londonPressure, londonWind);
                    Log.d("LocationData", "Updating UI for London");
                } else if (url.equals(observationUrls[2])) { // NewYork's URL
                    updateViewsForLocation(observation, newYorkDay, newYorkTemperature, newYorkPressure, newYorkWind);
                    Log.d("LocationData", "Updating UI for NY");
                }else if (url.equals(observationUrls[3])) { // Oman's URL
                    updateViewsForLocation(observation, omanDay, omanTemperature, omanPressure, omanWind);
                    Log.d("LocationData", "Updating UI for Oman");
                }else if (url.equals(observationUrls[4])) { // Mauritius's URL
                    updateViewsForLocation(observation, mauritiusDay, mauritiusTemperature, mauritiusPressure, mauritiusWind);
                    Log.d("LocationData", "Updating UI for Mauritius");
                }else if (url.equals(observationUrls[5])) { // Bangladesh's URL
                    updateViewsForLocation(observation, bangladeshDay, bangladeshTemperature, bangladeshPressure, bangladeshWind);
                    Log.d("LocationData", "Updating UI for Bangladesh");
                }
            }
        }

        private void updateViewsForLocation(Observations observation, TextView dayView, TextView temperatureView, TextView pressureView, TextView windView) {
            dayView.setText(String.format("%s", observation.getDay()));
            temperatureView.setText(String.format("%s", observation.getTemperature()));
            pressureView.setText(String.format("%s", observation.getPressure()));
            windView.setText(String.format("%s", observation.getWindSpeed()));
        }
    }

    private class Task implements Runnable {
        private String[] urls;

        public Task(String[] urls) {
            this.urls = urls;
        }

        @Override
        public void run() {
            Log.d("MyTag", "Task started");


            for (String url1 : urls) {
                // Clear previous forecasts for each URL
                forecasts.clear();
                // Fetch and process data from the current URL
                fetchDataAndParse(url1);
                final String result1 = formatForecasts();

                // Get the corresponding TextView for each URL
                TextView textView1 = getCorrespondingTextView(url1);
                if (textView1 != null) {
                    final TextView finalTextView = textView1;
                    // Update the UI with the formatted data for this URL
                    if (textView1 != null) {
                        Log.d("MyTag", "Updating TextView with data - forecasts");
                        runOnUiThread(() -> textView1.setText(result1));
                    } else {
                        Log.d("MyTag", "TextView reference is null");
                    }

                }
            }
        }

        private void fetchDataAndParse(String urlString) {
            try {
                URL url1 = new URL(urlString);
                URLConnection yc = url1.openConnection();
                try (InputStream in = yc.getInputStream()) {
                    parseDataF(in); // Parse the input stream
                }
            } catch (IOException e) {
                Log.e("MyTag", "IOException in fetchDataAndParse", e);
            }
        }

        // Convert the list of Forecasts to a String format for display
        private String formatForecasts() {
            StringBuilder formattedf = new StringBuilder();
            for (ThreeDayForecast threeDayForecast : forecasts) {
                formattedf.append("Day: ").append(threeDayForecast.getTitle()).append("\n")
                        .append("Description: ").append(threeDayForecast.getDescription()).append("\n")
                        .append("Date: ").append(threeDayForecast.getDate()).append("\n\n");
            }
            return formattedf.toString();
        }

        private TextView getCorrespondingTextView(String url1) {
            // Implement logic to return the corresponding TextView based on the URL
            if (url1.equals(forecastUrls[0])) {
                return glasgowFor;
            } else if (url1.equals(forecastUrls[1])) {
                return londonFor;
            } else if (url1.equals(forecastUrls[2])) {
                return newyorkFor;
            } else if (url1.equals(forecastUrls[3])) {
                return omanFor;
            } else if (url1.equals(forecastUrls[4])) {
                return mauritiusFor;
            } else if (url1.equals(forecastUrls[5])) {
                return bangladeshFor;
            }
            return null;
        }
    }




    private void parseDataF(InputStream in) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);
            int eventType = xpp.getEventType();
            ThreeDayForecast currentForecast = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        currentForecast = new ThreeDayForecast();
                    } else if (currentForecast != null) {
                        if (xpp.getName().equalsIgnoreCase("title")) {
                            currentForecast.setTitle(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            String description = xpp.nextText();
                            currentForecast.setDescription(description);

                            // Parse the description for various pieces of information
                            parseDescription(description, currentForecast);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            currentForecast.setDate(xpp.nextText());
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item") && currentForecast != null) {
                    forecasts.add(currentForecast);
                    currentForecast = null;
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("MyTag", "Error during parsing", e);
        }
        Log.d("MyTag", "End of document reached. Forecasts size: " + forecasts.size());
    }

    private void parseDescription(String description, ThreeDayForecast forecast) {
        // Temperature
        Pattern tempPattern = Pattern.compile("Minimum Temperature: (\\d+Â°C)");
        Matcher tempMatcher = tempPattern.matcher(description);
        if (tempMatcher.find()) {
            forecast.setMinimumTemperature(tempMatcher.group(1));
        }

        // Wind Direction
        Pattern windDirPattern = Pattern.compile("Wind Direction: ([a-zA-Z]+)");
        Matcher windDirMatcher = windDirPattern.matcher(description);
        if (windDirMatcher.find()) {
            forecast.setWindDirection(windDirMatcher.group(1));
        }

        // Wind Speed
        Pattern windSpeedPattern = Pattern.compile("Wind Speed: (\\d+mph)");
        Matcher windSpeedMatcher = windSpeedPattern.matcher(description);
        if (windSpeedMatcher.find()) {
            forecast.setWindSpeed(windSpeedMatcher.group(1));
        }

        // Visibility
        Pattern visibilityPattern = Pattern.compile("Visibility: (\\w+)");
        Matcher visibilityMatcher = visibilityPattern.matcher(description);
        if (visibilityMatcher.find()) {
            forecast.setVisibility(visibilityMatcher.group(1));
        }

        // Pressure
        Pattern pressurePattern = Pattern.compile("Pressure: (\\d+mb)");
        Matcher pressureMatcher = pressurePattern.matcher(description);
        if (pressureMatcher.find()) {
            forecast.setPressure(pressureMatcher.group(1));
        }

        // Humidity
        Pattern humidityPattern = Pattern.compile("Humidity: (\\d+)%");
        Matcher humidityMatcher = humidityPattern.matcher(description);
        if (humidityMatcher.find()) {
            forecast.setHumidity(humidityMatcher.group(1) + "%");
        }

        // UV Risk
        Pattern uvRiskPattern = Pattern.compile("UV Risk: (\\d+)");
        Matcher uvRiskMatcher = uvRiskPattern.matcher(description);
        if (uvRiskMatcher.find()) {
            forecast.setUvRisk(uvRiskMatcher.group(1));
        }

        // Pollution
        Pattern pollutionPattern = Pattern.compile("Pollution: (\\w+)");
        Matcher pollutionMatcher = pollutionPattern.matcher(description);
        if (pollutionMatcher.find()) {
            forecast.setPollution(pollutionMatcher.group(1));
        }

        // Sunset
        Pattern sunsetPattern = Pattern.compile("Sunset: (\\d{2}:\\d{2} GMT)");
        Matcher sunsetMatcher = sunsetPattern.matcher(description);
        if (sunsetMatcher.find()) {
            forecast.setSunset(sunsetMatcher.group(1));
        }
    }



    private void parseDataO(InputStream in, CopyOnWriteArrayList<Observations> currentObservations) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, null);
            int eventType = xpp.getEventType();
            Observations newObservations = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String localName = xpp.getName();

                    if ("item".equalsIgnoreCase(localName)) {
                        newObservations = new Observations(); // Create new observation instance
                    } else if (newObservations != null) {
                        if ("title".equalsIgnoreCase(localName)) {
                            String titleText = xpp.nextText();
                            // Extract day, assuming it's the first word before the '-'
                            String day = titleText.split(" -")[0];
                            newObservations.setDay(day);
                            Log.d("MyTag", "New item found!");
                        } else if ("description".equalsIgnoreCase(localName)) {
                            String descText = xpp.nextText();
                            // Use regex or string manipulation to extract specific parts
                            Pattern temperaturePattern = Pattern.compile("Temperature: (\\d+)(Â°C|°C)");
                            Matcher temperatureMatcher = temperaturePattern.matcher(descText);
                            if (temperatureMatcher.find()) {
                                String temperatureWithUnit = temperatureMatcher.group(1) + "°C"; // Normalize the temperature string
                                newObservations.setTemperature(temperatureWithUnit);
                                Log.d("MyTag", "New item found!");
                            }


                            Pattern pressurePattern = Pattern.compile("Pressure: (\\d+mb)");
                            Matcher pressureMatcher = pressurePattern.matcher(descText);
                            if (pressureMatcher.find()) {
                                newObservations.setPressure(pressureMatcher.group(1));
                                Log.d("MyTag", "New item found!");
                            }

                            Pattern windSpeedPattern = Pattern.compile("Wind Speed: (\\d+mph)");
                            Matcher windSpeedMatcher = windSpeedPattern.matcher(descText);
                            if (windSpeedMatcher.find()) {
                                newObservations.setWindSpeed(windSpeedMatcher.group(1));
                                Log.d("MyTag", "New item found!");
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("item".equalsIgnoreCase(xpp.getName()) && currentObservations != null) {
                        currentObservations.add(newObservations);
                        newObservations = null;
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e("MyTag", "Parsing error" + e.toString());
        } catch (IOException e) {
            Log.e("MyTag", "IO error during parsing");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleMap != null) {
            updateMapLocation(weatherViewFlipper1.getDisplayedChild());
        }
    }


}