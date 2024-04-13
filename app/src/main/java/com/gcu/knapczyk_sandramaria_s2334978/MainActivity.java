package com.gcu.knapczyk_sandramaria_s2334978;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.ViewFlipper;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap googleMap;
    private LinearLayout mainView;
    private ViewSwitcher viewSwitcher;
    private FragmentContainerView fragmentContainerView;
    private ViewFlipper weatherViewFlipper1, weatherViewFlipper2;
    private Button backButton, forButton;
    private ImageButton nextButton1, prevButton1, nextButton2, prevButton2;
    private ImageButton infoGlasgow1, infoGlasgow2, infoGlasgow3, infoLondon1, infoLondon2, infoLondon3, infoNewYork1, infoNewYork2, infoNewYork3, infoOman1, infoOman2, infoOman3, infoMauritius1, infoMauritius2, infoMauritius3, infoBangladesh1, infoBangladesh2, infoBangladesh3;
    private Spinner locationSpinner;
    // Glasgow 3-day forecast text views
    private TextView glasgowDay1, glasgowMax1, glasgowMin1, glasgowDay2, glasgowMax2, glasgowMin2, glasgowDay3, glasgowMax3, glasgowMin3;

    // London 3-day forecast text views
    private TextView londonDay1, londonMax1, londonMin1, londonDay2, londonMax2, londonMin2, londonDay3, londonMax3, londonMin3;

    // New York 3-day forecast text views
    private TextView newYorkDay1, newYorkMax1, newYorkMin1, newYorkDay2, newYorkMax2, newYorkMin2, newYorkDay3, newYorkMax3, newYorkMin3;

    // Oman 3-day forecast text views
    private TextView omanDay1, omanMax1, omanMin1, omanDay2, omanMax2, omanMin2, omanDay3, omanMax3, omanMin3;

    // Mauritius 3-day forecast text views
    private TextView mauritiusDay1, mauritiusMax1, mauritiusMin1, mauritiusDay2, mauritiusMax2, mauritiusMin2, mauritiusDay3, mauritiusMax3, mauritiusMin3;

    // Bangladesh 3-day forecast text views
    private TextView bangladeshDay1, bangladeshMax1, bangladeshMin1, bangladeshDay2, bangladeshMax2, bangladeshMin2, bangladeshDay3, bangladeshMax3, bangladeshMin3;

    // Glasgow observation textviews
    private TextView glasgowDay, glasgowTemperature, glasgowPressure, glasgowWind;

    // London observation textviews
    private TextView londonDay, londonTemperature, londonPressure, londonWind;

    // New York observation textviews
    private TextView newYorkDay, newYorkTemperature, newYorkPressure, newYorkWind;

    // Oman observation textviews
    private TextView omanDay, omanTemperature, omanPressure, omanWind;

    // Mauritius observation textviews
    private TextView mauritiusDay, mauritiusTemperature, mauritiusPressure, mauritiusWind;

    // Bangladesh observation textviews
    private TextView bangladeshDay, bangladeshTemperature, bangladeshPressure, bangladeshWind;

    // Image views for forecast
    private ImageView glasgowIcon1, glasgowIcon2, glasgowIcon3, londonIcon1, londonIcon2, londonIcon3, newYorkIcon1, newYorkIcon2, newYorkIcon3, omanIcon1, omanIcon2, omanIcon3, mauritiusIcon1, mauritiusIcon2, mauritiusIcon3, bangladeshIcon1, bangladeshIcon2, bangladeshIcon3;


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

    private Map<String, CopyOnWriteArrayList<ThreeDayForecast>> forecastMap = new ConcurrentHashMap<>();
    private Map<String, CopyOnWriteArrayList<Observations>> observationsMap = new ConcurrentHashMap<>();

    //
    HashMap<Integer, Pair<String, Integer>> buttonToForecastInfo = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Base_Theme_Knapczyk_SandraMaria_S2334978);
        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.mainView);
        mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.clouds_blue_sky));

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

        // Set text views for 3day forecast
        // Initialize Glasgow TextViews
        glasgowDay1 = findViewById(R.id.glasgowDay1);
        glasgowMax1 = findViewById(R.id.glasgowMax1);
        glasgowMin1 = findViewById(R.id.glasgowMin1);
        glasgowDay2 = findViewById(R.id.glasgowDay2);
        glasgowMax2 = findViewById(R.id.glasgowMax2);
        glasgowMin2 = findViewById(R.id.glasgowMin2);
        glasgowDay3 = findViewById(R.id.glasgowDay3);
        glasgowMax3 = findViewById(R.id.glasgowMax3);
        glasgowMin3 = findViewById(R.id.glasgowMin3);

        // Initialize London TextViews
        londonDay1 = findViewById(R.id.londonDay1);
        londonMax1 = findViewById(R.id.londonMax1);
        londonMin1 = findViewById(R.id.londonMin1);
        londonDay2 = findViewById(R.id.londonDay2);
        londonMax2 = findViewById(R.id.londonMax2);
        londonMin2 = findViewById(R.id.londonMin2);
        londonDay3 = findViewById(R.id.londonDay3);
        londonMax3 = findViewById(R.id.londonMax3);
        londonMin3 = findViewById(R.id.londonMin3);

        // Initialize New York TextViews
        newYorkDay1 = findViewById(R.id.newYorkDay1);
        newYorkMax1 = findViewById(R.id.newYorkMax1);
        newYorkMin1 = findViewById(R.id.newYorkMin1);
        newYorkDay2 = findViewById(R.id.newYorkDay2);
        newYorkMax2 = findViewById(R.id.newYorkMax2);
        newYorkMin2 = findViewById(R.id.newYorkMin2);
        newYorkDay3 = findViewById(R.id.newYorkDay3);
        newYorkMax3 = findViewById(R.id.newYorkMax3);
        newYorkMin3 = findViewById(R.id.newYorkMin3);

        // Initialize Oman TextViews
        omanDay1 = findViewById(R.id.omanDay1);
        omanMax1 = findViewById(R.id.omanMax1);
        omanMin1 = findViewById(R.id.omanMin1);
        omanDay2 = findViewById(R.id.omanDay2);
        omanMax2 = findViewById(R.id.omanMax2);
        omanMin2 = findViewById(R.id.omanMin2);
        omanDay3 = findViewById(R.id.omanDay3);
        omanMax3 = findViewById(R.id.omanMax3);
        omanMin3 = findViewById(R.id.omanMin3);

        // Initialize Mauritius TextViews
        mauritiusDay1 = findViewById(R.id.mauritiusDay1);
        mauritiusMax1 = findViewById(R.id.mauritiusMax1);
        mauritiusMin1 = findViewById(R.id.mauritiusMin1);
        mauritiusDay2 = findViewById(R.id.mauritiusDay2);
        mauritiusMax2 = findViewById(R.id.mauritiusMax2);
        mauritiusMin2 = findViewById(R.id.mauritiusMin2);
        mauritiusDay3 = findViewById(R.id.mauritiusDay3);
        mauritiusMax3 = findViewById(R.id.mauritiusMax3);
        mauritiusMin3 = findViewById(R.id.mauritiusMin3);

        // Initialize Bangladesh TextViews
        bangladeshDay1 = findViewById(R.id.bangladeshDay1);
        bangladeshMax1 = findViewById(R.id.bangladeshMax1);
        bangladeshMin1 = findViewById(R.id.bangladeshMin1);
        bangladeshDay2 = findViewById(R.id.bangladeshDay2);
        bangladeshMax2 = findViewById(R.id.bangladeshMax2);
        bangladeshMin2 = findViewById(R.id.bangladeshMin2);
        bangladeshDay3 = findViewById(R.id.bangladeshDay3);
        bangladeshMax3 = findViewById(R.id.bangladeshMax3);
        bangladeshMin3 = findViewById(R.id.bangladeshMin3);

        // Initialise image views for the weather icons
        glasgowIcon1 = findViewById(R.id.glasgowIcon1);
        glasgowIcon2 = findViewById(R.id.glasgowIcon2);
        glasgowIcon3 = findViewById(R.id.glasgowIcon3);
        londonIcon1 = findViewById(R.id.londonIcon1);
        londonIcon2 = findViewById(R.id.londonIcon2);
        londonIcon3 = findViewById(R.id.londonIcon3);
        newYorkIcon1 = findViewById(R.id.newYorkIcon1);
        newYorkIcon2 = findViewById(R.id.newYorkIcon2);
        newYorkIcon3 = findViewById(R.id.newYorkIcon3);
        omanIcon1 = findViewById(R.id.omanIcon1);
        omanIcon2 = findViewById(R.id.omanIcon2);
        omanIcon3 = findViewById(R.id.omanIcon3);
        mauritiusIcon1 = findViewById(R.id.mauritiusIcon1);
        mauritiusIcon2 = findViewById(R.id.mauritiusIcon2);
        mauritiusIcon3 = findViewById(R.id.mauritiusIcon3);
        bangladeshIcon1 = findViewById(R.id.bangladeshIcon1);
        bangladeshIcon2 = findViewById(R.id.bangladeshIcon2);
        bangladeshIcon3 = findViewById(R.id.bangladeshIcon3);

        // Initialize Glasgow ImageButtons
        infoGlasgow1 = findViewById(R.id.infoGlasgow1);
        infoGlasgow1.setOnClickListener(this);
        infoGlasgow2 = findViewById(R.id.infoGlasgow2);
        infoGlasgow2.setOnClickListener(this);
        infoGlasgow3 = findViewById(R.id.infoGlasgow3);
        infoGlasgow3.setOnClickListener(this);

        // Initialize London ImageButtons
        infoLondon1 = findViewById(R.id.infoLondon1);
        infoLondon1.setOnClickListener(this);
        infoLondon2 = findViewById(R.id.infoLondon2);
        infoLondon2.setOnClickListener(this);
        infoLondon3 = findViewById(R.id.infoLondon3);
        infoLondon3.setOnClickListener(this);

        // Initialize New York ImageButtons
        infoNewYork1 = findViewById(R.id.infoNewYork1);
        infoNewYork1.setOnClickListener(this);
        infoNewYork2 = findViewById(R.id.infoNewYork2);
        infoNewYork2.setOnClickListener(this);
        infoNewYork3 = findViewById(R.id.infoNewYork3);
        infoNewYork3.setOnClickListener(this);

        // Initialize Oman ImageButtons
        infoOman1 = findViewById(R.id.infoOman1);
        infoOman1.setOnClickListener(this);
        infoOman2 = findViewById(R.id.infoOman2);
        infoOman2.setOnClickListener(this);
        infoOman3 = findViewById(R.id.infoOman3);
        infoOman3.setOnClickListener(this);

        // Initialize Mauritius ImageButtons
        infoMauritius1 = findViewById(R.id.infoMauritius1);
        infoMauritius1.setOnClickListener(this);
        infoMauritius2 = findViewById(R.id.infoMauritius2);
        infoMauritius2.setOnClickListener(this);
        infoMauritius3 = findViewById(R.id.infoMauritius3);
        infoMauritius3.setOnClickListener(this);

        // Initialize Bangladesh ImageButtons
        infoBangladesh1 = findViewById(R.id.infoBangladesh1);
        infoBangladesh1.setOnClickListener(this);
        infoBangladesh2 = findViewById(R.id.infoBangladesh2);
        infoBangladesh2.setOnClickListener(this);
        infoBangladesh3 = findViewById(R.id.infoBangladesh3);
        infoBangladesh3.setOnClickListener(this);


        // Map image buttons for additional info
        buttonToForecastInfo.put(R.id.infoGlasgow1, new Pair<>("Glasgow", 0));
        buttonToForecastInfo.put(R.id.infoGlasgow2, new Pair<>("Glasgow", 1));
        buttonToForecastInfo.put(R.id.infoGlasgow3, new Pair<>("Glasgow", 2));
        buttonToForecastInfo.put(R.id.infoLondon1, new Pair<>("London", 0));
        buttonToForecastInfo.put(R.id.infoLondon2, new Pair<>("London", 1));
        buttonToForecastInfo.put(R.id.infoLondon3, new Pair<>("London", 2));
        buttonToForecastInfo.put(R.id.infoNewYork1, new Pair<>("New York", 0));
        buttonToForecastInfo.put(R.id.infoNewYork2, new Pair<>("New York", 1));
        buttonToForecastInfo.put(R.id.infoNewYork3, new Pair<>("New York", 2));
        buttonToForecastInfo.put(R.id.infoOman1, new Pair<>("Oman", 0));
        buttonToForecastInfo.put(R.id.infoOman2, new Pair<>("Oman", 1));
        buttonToForecastInfo.put(R.id.infoOman3, new Pair<>("Oman", 2));
        buttonToForecastInfo.put(R.id.infoMauritius1, new Pair<>("Mauritius", 0));
        buttonToForecastInfo.put(R.id.infoMauritius2, new Pair<>("Mauritius", 1));
        buttonToForecastInfo.put(R.id.infoMauritius3, new Pair<>("Mauritius", 2));
        buttonToForecastInfo.put(R.id.infoBangladesh1, new Pair<>("Bangladesh", 0));
        buttonToForecastInfo.put(R.id.infoBangladesh2, new Pair<>("Bangladesh", 1));
        buttonToForecastInfo.put(R.id.infoBangladesh3, new Pair<>("Bangladesh", 2));


        // Set up link to layout views
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

        //startProgressObservations();
        startProgressObservations();

        //Initialise maps fragment
        fragmentContainerView = findViewById(R.id.map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // to the OnMapReadyCallback
        }
    }

    @Override
    public void onClick(View v) {
        // Debug log to check the contents of the map
        for (Map.Entry<Integer, Pair<String, Integer>> entry : buttonToForecastInfo.entrySet()) {
            Log.d("MyTag", "Button ID: " + entry.getKey() + ", Info: " + entry.getValue().first + ", " + entry.getValue().second);
        }

        Pair<String, Integer> info = buttonToForecastInfo.get(v.getId());
        if (info != null) {
            // Log the value of info.first
            Log.d("MyTag", "Info.first: " + info.first);

            // Get the forecasts for the location
            CopyOnWriteArrayList<ThreeDayForecast> forecasts = forecastMap.get(info.first);

            // Log the contents of forecastMap
            Log.d("MyTag", "ForecastMap: " + forecastMap);

            // Get the forecasts for the location
            ThreeDayForecast forecast = null;

            // Check if forecasts is not null and dayIndex is within bounds
            if (forecasts != null && info.second < forecasts.size()) {
                // Show forecast details
                forecast = showForecastDetails(info.first, info.second);
            }
        } else {
            // Handle other buttons based on their IDs
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
    }

    private ThreeDayForecast showForecastDetails(String location, int dayIndex) {
        CopyOnWriteArrayList<ThreeDayForecast> forecasts = forecastMap.get(location);
        if (forecasts != null && dayIndex < forecasts.size()) {
            ThreeDayForecast forecast = forecasts.get(dayIndex);
            // Create a detailed message from the forecast description attributes
            String details = buildDetailMessage(forecast);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(location + " Detailed Forecast for " + forecast.getDay());
            builder.setMessage(details);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();

            return forecast;  // Return the forecast object
        } else {
            Toast.makeText(this, "Forecast data not available", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private String buildDetailMessage(ThreeDayForecast forecast) {
        Log.d("WeatherDetails", "Building message for: " + forecast.getDay());
        StringBuilder details = new StringBuilder();
        if (forecast.getWindDirection() != null) details.append("Wind Direction: ").append(forecast.getWindDirection()).append("\n");
        if (forecast.getWindSpeed() != null) details.append("Wind Speed: ").append(forecast.getWindSpeed()).append("\n");
        if (forecast.getVisibility() != null) details.append("Visibility: ").append(forecast.getVisibility()).append("\n");
        if (forecast.getPressure() != null) details.append("Pressure: ").append(forecast.getPressure()).append("\n");
        if (forecast.getHumidity() != null) details.append("Humidity: ").append(forecast.getHumidity()).append("\n");
        if (forecast.getUvRisk() != null) details.append("UV Risk: ").append(forecast.getUvRisk()).append("\n");
        if (forecast.getPollution() != null) details.append("Pollution: ").append(forecast.getPollution()).append("\n");
        if (forecast.getSunset() != null) details.append("Sunset: ").append(forecast.getSunset()).append("\n");
        return details.toString();
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
                } else if (url.equals(observationUrls[3])) { // Oman's URL
                    updateViewsForLocation(observation, omanDay, omanTemperature, omanPressure, omanWind);
                    Log.d("LocationData", "Updating UI for Oman");
                } else if (url.equals(observationUrls[4])) { // Mauritius's URL
                    updateViewsForLocation(observation, mauritiusDay, mauritiusTemperature, mauritiusPressure, mauritiusWind);
                    Log.d("LocationData", "Updating UI for Mauritius");
                } else if (url.equals(observationUrls[5])) { // Bangladesh's URL
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

    private class Task implements Runnable {
        private String[] urls;

        public Task(String[] urls) {
            this.urls = urls;
        }

        @Override
        public void run() {
            Log.d("MyTag", "Forecast Task started");

            for (String url : urls) {
                try {
                    // Resolve URL to location name
                    String locationName = getLocationNameFromUrl(url);

                    // Fetch and parse data, then put it into the map
                    forecastMap.put(locationName, fetchDataAndParse(url));

                    runOnUiThread(() -> updateForecastViews(locationName, forecastMap.get(locationName)));

                } catch (Exception e) {
                    Log.e("MyTag", "Error in fetching or parsing data: " + e.getMessage());
                }
            }
        }

        private CopyOnWriteArrayList<ThreeDayForecast> fetchDataAndParse(String urlString) {
            CopyOnWriteArrayList<ThreeDayForecast> currentForecasts = new CopyOnWriteArrayList<>();
            try {
                URL url = new URL(urlString);
                URLConnection yc = url.openConnection();
                try (InputStream in = yc.getInputStream()) {
                    parseDataF(in, currentForecasts); // Parse the input stream
                }
            } catch (IOException e) {
                Log.e("MyTag", "IOException in fetchDataAndParse", e);
            }
            return currentForecasts;
        }

        private void updateForecastViews(String locationName, CopyOnWriteArrayList<ThreeDayForecast> forecasts) {
            if (forecasts == null || forecasts.isEmpty()) {
                Log.e("MyTag", "Forecast data is empty for " + locationName);
                return;
            }

            switch (locationName) {
                case "Glasgow":
                    updateForecastTextViews(forecasts, glasgowDay1, glasgowMax1, glasgowMin1, glasgowIcon1, glasgowDay2, glasgowMax2, glasgowMin2, glasgowIcon2, glasgowDay3, glasgowMax3, glasgowMin3, glasgowIcon3);
                    break;
                case "London":
                    updateForecastTextViews(forecasts, londonDay1, londonMax1, londonMin1, londonIcon1, londonDay2, londonMax2, londonMin2, londonIcon2, londonDay3, londonMax3, londonMin3, londonIcon3);
                    break;
                case "New York":
                    updateForecastTextViews(forecasts, newYorkDay1, newYorkMax1, newYorkMin1, newYorkIcon1, newYorkDay2, newYorkMax2, newYorkMin2, newYorkIcon2, newYorkDay3, newYorkMax3, newYorkMin3, newYorkIcon3);
                    break;
                case "Oman":
                    updateForecastTextViews(forecasts, omanDay1, omanMax1, omanMin1, omanIcon1, omanDay2, omanMax2, omanMin2, omanIcon2, omanDay3, omanMax3, omanMin3, omanIcon3);
                    break;
                case "Mauritius":
                    updateForecastTextViews(forecasts, mauritiusDay1, mauritiusMax1, mauritiusMin1, mauritiusIcon1, mauritiusDay2, mauritiusMax2, mauritiusMin2, mauritiusIcon2, mauritiusDay3, mauritiusMax3, mauritiusMin3, mauritiusIcon3);
                    break;
                case "Bangladesh":
                    updateForecastTextViews(forecasts, bangladeshDay1, bangladeshMax1, bangladeshMin1, bangladeshIcon1, bangladeshDay2, bangladeshMax2, bangladeshMin2, bangladeshIcon2, bangladeshDay3, bangladeshMax3, bangladeshMin3, bangladeshIcon3);
                    break;
                default:
                    Log.e("MyTag", "Unknown location name: " + locationName);
                    break;
            }
        }

        private void updateForecastTextViews(CopyOnWriteArrayList<ThreeDayForecast> forecasts,
                                             TextView day1, TextView max1, TextView min1, ImageView icon1,
                                             TextView day2, TextView max2, TextView min2, ImageView icon2,
                                             TextView day3, TextView max3, TextView min3, ImageView icon3) {
            // Ensure we have at least three forecasts to update the UI correctly
            if (forecasts.size() >= 3) {
                ThreeDayForecast forecastDay1 = forecasts.get(0); // First day forecast
                day1.setText(forecastDay1.getDay());
                max1.setText(forecastDay1.getMaximumTemperature());
                min1.setText(forecastDay1.getMinimumTemperature());
                icon1.setImageResource(getIconResourceId(forecastDay1.getWeatherCondition()));

                ThreeDayForecast forecastDay2 = forecasts.get(1); // Second day forecast
                day2.setText(forecastDay2.getDay());
                max2.setText(forecastDay2.getMaximumTemperature());
                min2.setText(forecastDay2.getMinimumTemperature());
                icon2.setImageResource(getIconResourceId(forecastDay2.getWeatherCondition()));

                ThreeDayForecast forecastDay3 = forecasts.get(2); // Third day forecast
                day3.setText(forecastDay3.getDay());
                max3.setText(forecastDay3.getMaximumTemperature());
                min3.setText(forecastDay3.getMinimumTemperature());
                icon3.setImageResource(getIconResourceId(forecastDay3.getWeatherCondition()));
            } else {
                Log.e("MyTag", "Not enough forecast data to update all days.");
            }
        }

        public void updateWeatherIcon(ImageView imageView, String weatherCondition) {
            int resourceId = getIconResourceId(weatherCondition);
            imageView.setImageResource(resourceId);
        }

        private int getIconResourceId(String weatherCondition) {
            switch (weatherCondition) {
                case "Sunny":
                    return R.drawable.sunny;
                case "Sunny Intervals":
                    return R.drawable.sunny_intervals;
                case "Light Cloud":
                    return R.drawable.fully_cloudy;
                case "Partly Cloudy":
                    return R.drawable.cloudy;
                case "Light Rain Showers":
                    return R.drawable.rain;
                case "Light Rain":
                    return R.drawable.rain;
                case "Drizzle":
                    return R.drawable.light_rain;
                case "Heavy Rain":
                    return R.drawable.heavy_rain;
                case "Thundery Showers":
                    return R.drawable.thundery;
                case "Mist":
                    return R.drawable.mist;
                default:
                    return R.drawable.question; // Default icon
            }
        }

    }

    private String getLocationNameFromUrl(String url) {
        if (url.equals(forecastUrls[0])) return "Glasgow";
        if (url.equals(forecastUrls[1])) return "London";
        if (url.equals(forecastUrls[2])) return "New York";
        if (url.equals(forecastUrls[3])) return "Oman";
        if (url.equals(forecastUrls[4])) return "Mauritius";
        if (url.equals(forecastUrls[5])) return "Bangladesh";
        return null;
    }


    private void parseDataF(InputStream in, CopyOnWriteArrayList<ThreeDayForecast> currentForecasts) {
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

    private void parseDescription(String description, ThreeDayForecast forecast) {

        // Wind Direction
        Pattern windDirPattern = Pattern.compile("Wind Direction: (\\w+ \\w+)");
        Matcher windDirMatcher = windDirPattern.matcher(description);
        if (windDirMatcher.find()) {
            forecast.setWindDirection(windDirMatcher.group(1));
            Log.d("WeatherParse", "Parsed Wind Direction: " + windDirMatcher.group(1));
        }

        // Wind Speed
        Pattern windSpeedPattern = Pattern.compile("Wind Speed: (\\d+)mph");
        Matcher windSpeedMatcher = windSpeedPattern.matcher(description);
        if (windSpeedMatcher.find()) {
            forecast.setWindSpeed(windSpeedMatcher.group(1) + "mph");
            Log.d("WeatherParse", "Parsed Wind Speed: " + windSpeedMatcher.group(1) + "mph");
        }

        // Visibility
        Pattern visibilityPattern = Pattern.compile("Visibility: (\\w+)");
        Matcher visibilityMatcher = visibilityPattern.matcher(description);
        if (visibilityMatcher.find()) {
            forecast.setVisibility(visibilityMatcher.group(1));
            Log.d("WeatherParse", "Parsed Visibility: " + visibilityMatcher.group(1));
        }

        // Pressure
        Pattern pressurePattern = Pattern.compile("Pressure: (\\d+)mb");
        Matcher pressureMatcher = pressurePattern.matcher(description);
        if (pressureMatcher.find()) {
            forecast.setPressure(pressureMatcher.group(1) + "mb");
            Log.d("WeatherParse", "Parsed Pressure: " + pressureMatcher.group(1) + "mb");
        }

        // Humidity
        Pattern humidityPattern = Pattern.compile("Humidity: (\\d+)%");
        Matcher humidityMatcher = humidityPattern.matcher(description);
        if (humidityMatcher.find()) {
            forecast.setHumidity(humidityMatcher.group(1) + "%");
            Log.d("WeatherParse", "Parsed Humidity: " + humidityMatcher.group(1) + "%");
        }

        // UV Risk
        Pattern uvRiskPattern = Pattern.compile("UV Risk: (\\d+)");
        Matcher uvRiskMatcher = uvRiskPattern.matcher(description);
        if (uvRiskMatcher.find()) {
            forecast.setUvRisk(uvRiskMatcher.group(1));
            Log.d("WeatherParse", "Parsed UV Risk: " + uvRiskMatcher.group(1));
        }

        // Pollution
        Pattern pollutionPattern = Pattern.compile("Pollution: (\\w+)");
        Matcher pollutionMatcher = pollutionPattern.matcher(description);
        if (pollutionMatcher.find()) {
            forecast.setPollution(pollutionMatcher.group(1));
            Log.d("WeatherParse", "Parsed Pollution: " + pollutionMatcher.group(1));
        }

        // Sunset
        Pattern sunsetPattern = Pattern.compile("Sunset: (\\d{2}:\\d{2} [A-Z]+)");
        Matcher sunsetMatcher = sunsetPattern.matcher(description);
        if (sunsetMatcher.find()) {
            forecast.setSunset(sunsetMatcher.group(1));
            Log.d("WeatherParse", "Parsed Sunset: " + sunsetMatcher.group(1));
        }
    }
}