package com.gcu.knapczyk_sandramaria_s2334978;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap googleMap;
    private ViewSwitcher viewSwitcher;
    private ViewFlipper weatherViewFlipper1, weatherViewFlipper2;
    private Button backButton, forButton;
    private ImageButton nextButton1, prevButton1, nextButton2, prevButton2;
    private ImageButton calendarButton, notesButton;
    private Spinner locationSpinner;
    private TextView dayDate, dayDate1;
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

    private final String[] observationUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"
    };

    private final String[] forecastUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    private final Map<String, CopyOnWriteArrayList<ThreeDayForecast>> forecastMap = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArrayList<Observations>> observationsMap = new ConcurrentHashMap<>();

    //
    HashMap<Integer, Pair<String, Integer>> buttonToForecastInfo = new HashMap<>();
    private Map<String, TextView[]> urlToTextViewsMap;

    private ScheduledExecutorService scheduler;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Base_Theme_Knapczyk_SandraMaria_S2334978);
        setContentView(R.layout.activity_main);

        LinearLayout mainView = findViewById(R.id.mainView);
        mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.clouds_blue_sky));

        // Initialize the scheduler and handler
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.mainHandler = new Handler(Looper.getMainLooper());

        // Initialize TextViews for 6 locations
        // Glasgow observation textviews
        TextView glasgowDay = findViewById(R.id.glasgowDay);
        TextView glasgowTemperature = findViewById(R.id.glasgowTemperature);
        TextView glasgowPressure = findViewById(R.id.glasgowPressure);
        TextView glasgowWind = findViewById(R.id.glasgowWind);

        // London observation textviews
        TextView londonDay = findViewById(R.id.londonDay);
        TextView londonTemperature = findViewById(R.id.londonTemperature);
        TextView londonPressure = findViewById(R.id.londonPressure);
        TextView londonWind = findViewById(R.id.londonWind);

        // New York observation textviews
        TextView newYorkDay = findViewById(R.id.newYorkDay);
        TextView newYorkTemperature = findViewById(R.id.newYorkTemperature);
        TextView newYorkPressure = findViewById(R.id.newYorkPressure);
        TextView newYorkWind = findViewById(R.id.newYorkWind);

        // Oman observation textviews
        TextView omanDay = findViewById(R.id.omanDay);
        TextView omanTemperature = findViewById(R.id.omanTemperature);
        TextView omanPressure = findViewById(R.id.omanPressure);
        TextView omanWind = findViewById(R.id.omanWind);

        // Mauritius observation textviews
        TextView mauritiusDay = findViewById(R.id.mauritiusDay);
        TextView mauritiusTemperature = findViewById(R.id.mauritiusTemperature);
        TextView mauritiusPressure = findViewById(R.id.mauritiusPressure);
        TextView mauritiusWind = findViewById(R.id.mauritiusWind);

        // Bangladesh observation textviews
        TextView bangladeshDay = findViewById(R.id.bangladeshDay);
        TextView bangladeshTemperature = findViewById(R.id.bangladeshTemperature);
        TextView bangladeshPressure = findViewById(R.id.bangladeshPressure);
        TextView bangladeshWind = findViewById(R.id.bangladeshWind);

        // Initialize the map
        urlToTextViewsMap = new HashMap<>();
        urlToTextViewsMap.put(observationUrls[0], new TextView[]{glasgowDay, glasgowTemperature, glasgowPressure, glasgowWind});
        urlToTextViewsMap.put(observationUrls[1], new TextView[]{londonDay, londonTemperature, londonPressure, londonWind});
        urlToTextViewsMap.put(observationUrls[2], new TextView[]{newYorkDay, newYorkTemperature, newYorkPressure, newYorkWind});
        urlToTextViewsMap.put(observationUrls[3], new TextView[]{omanDay, omanTemperature, omanPressure, omanWind});
        urlToTextViewsMap.put(observationUrls[4], new TextView[]{mauritiusDay, mauritiusTemperature, mauritiusPressure, mauritiusWind});
        urlToTextViewsMap.put(observationUrls[5], new TextView[]{bangladeshDay, bangladeshTemperature, bangladeshPressure, bangladeshWind});

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
        ImageButton infoGlasgow1 = findViewById(R.id.infoGlasgow1);
        infoGlasgow1.setOnClickListener(this);
        ImageButton infoGlasgow2 = findViewById(R.id.infoGlasgow2);
        infoGlasgow2.setOnClickListener(this);
        ImageButton infoGlasgow3 = findViewById(R.id.infoGlasgow3);
        infoGlasgow3.setOnClickListener(this);

        // Initialize London ImageButtons
        ImageButton infoLondon1 = findViewById(R.id.infoLondon1);
        infoLondon1.setOnClickListener(this);
        ImageButton infoLondon2 = findViewById(R.id.infoLondon2);
        infoLondon2.setOnClickListener(this);
        ImageButton infoLondon3 = findViewById(R.id.infoLondon3);
        infoLondon3.setOnClickListener(this);

        // Initialize New York ImageButtons
        ImageButton infoNewYork1 = findViewById(R.id.infoNewYork1);
        infoNewYork1.setOnClickListener(this);
        ImageButton infoNewYork2 = findViewById(R.id.infoNewYork2);
        infoNewYork2.setOnClickListener(this);
        ImageButton infoNewYork3 = findViewById(R.id.infoNewYork3);
        infoNewYork3.setOnClickListener(this);

        // Initialize Oman ImageButtons
        ImageButton infoOman1 = findViewById(R.id.infoOman1);
        infoOman1.setOnClickListener(this);
        ImageButton infoOman2 = findViewById(R.id.infoOman2);
        infoOman2.setOnClickListener(this);
        ImageButton infoOman3 = findViewById(R.id.infoOman3);
        infoOman3.setOnClickListener(this);

        // Initialize Mauritius ImageButtons
        ImageButton infoMauritius1 = findViewById(R.id.infoMauritius1);
        infoMauritius1.setOnClickListener(this);
        ImageButton infoMauritius2 = findViewById(R.id.infoMauritius2);
        infoMauritius2.setOnClickListener(this);
        ImageButton infoMauritius3 = findViewById(R.id.infoMauritius3);
        infoMauritius3.setOnClickListener(this);

        // Initialize Bangladesh ImageButtons
        ImageButton infoBangladesh1 = findViewById(R.id.infoBangladesh1);
        infoBangladesh1.setOnClickListener(this);
        ImageButton infoBangladesh2 = findViewById(R.id.infoBangladesh2);
        infoBangladesh2.setOnClickListener(this);
        ImageButton infoBangladesh3 = findViewById(R.id.infoBangladesh3);
        infoBangladesh3.setOnClickListener(this);

        // Initialise Image Buttons for calendar and notes app
        calendarButton = findViewById(R.id.calendarButton);
        notesButton = findViewById(R.id.notesButton);
        notesButton.setOnClickListener(this);
        calendarButton.setOnClickListener(this);

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

        dayDate = (findViewById(R.id.dayDate));
        dayDate1 = (findViewById(R.id.dayDate1));

        // Set up link to layout views
        viewSwitcher = findViewById(R.id.viewSwitcher);
        if (viewSwitcher == null) {
            Toast.makeText(getApplicationContext(), "Null ViewSwitcher",
                    Toast.LENGTH_LONG).show();
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
                // Update view flippers to chosen locations
                weatherViewFlipper1.setDisplayedChild(position);
                weatherViewFlipper2.setDisplayedChild(position);
                // Update the map location
                updateMapLocation(position);
                updateDateDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Display the first child and map view by default
                weatherViewFlipper1.setDisplayedChild(0);
                updateMapLocation(0);
                updateDateDisplay();
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

        // Initialize Tasks with the URLs
        ObservationThread observationThread = new ObservationThread(observationUrls, scheduler, mainHandler);
        ForecastThread forecastThread = new ForecastThread(forecastUrls, scheduler, mainHandler);
        // Start ObservationThread
        observationThread.start();
        forecastThread.start();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // to the OnMapReadyCallback
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Properly shutdown the executor when the activity is destroyed
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
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
            // Retrieve the location name and day index
            String locationName = info.first;
            Integer dayIndex = info.second;

            // Log info values
            Log.d("MyTag", "Location: " + locationName + ", Day Index: " + dayIndex);

            // Retrieve forecasts for the location and log the entire map once
            CopyOnWriteArrayList<ThreeDayForecast> forecasts = forecastMap.get(locationName);
            Log.d("MyTag", "ForecastMap: " + forecastMap);

            // Handle the forecast retrieval and display
            if (forecasts != null && dayIndex < forecasts.size()) {
                // Show forecast details if valid
                showForecastDetails(locationName, dayIndex);
            } else {
                Log.d("MyTag", "No forecast available for " + locationName + " on day index " + dayIndex);
            }
        } else {
            // Handle other buttons based on their IDs
            if (v == forButton) {
                viewSwitcher.showNext();
                startProgressForecasts();
                Log.d("MyTag", "go to forecast screen");
            } else if (v == nextButton1 || v == nextButton2) {
                int nextPosition = (weatherViewFlipper1.getDisplayedChild() + 1) % weatherViewFlipper1.getChildCount();
                weatherViewFlipper1.setDisplayedChild(nextPosition);
                weatherViewFlipper2.setDisplayedChild(nextPosition);
                locationSpinner.setSelection(nextPosition);
                updateMapLocation(nextPosition);
                updateDateDisplay();
                Log.d("MyTag", "next screen");
            } else if (v == prevButton1 || v == prevButton2) {
                int prevPosition = (weatherViewFlipper1.getDisplayedChild() - 1 + weatherViewFlipper1.getChildCount()) % weatherViewFlipper1.getChildCount();
                weatherViewFlipper1.setDisplayedChild(prevPosition);
                weatherViewFlipper2.setDisplayedChild(prevPosition);
                locationSpinner.setSelection(prevPosition);
                updateMapLocation(prevPosition);
                updateDateDisplay();
                Log.d("MyTag", "prev screen");
            } else if (v == backButton) {
                viewSwitcher.showPrevious();
                startProgressObservations();
                Log.d("MyTag", "back to start screen");
            } else if (v == calendarButton){
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                startActivity(calendarIntent);
            } else if (v == notesButton){
                Intent notesIntent = new Intent(Intent.ACTION_SEND);
                notesIntent.setType("text/plain");
                notesIntent.putExtra(Intent.EXTRA_TEXT, "Your text here");
                notesIntent.setPackage("com.google.android.keep"); // This line targets Google Keep specifically
                try {
                    startActivity(notesIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Google Keep is not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showForecastDetails(String location, int dayIndex) {
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

        } else {
            Toast.makeText(this, "Forecast data not available", Toast.LENGTH_SHORT).show();
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
        // Create a new ObservationThread object with dependencies
        ObservationThread observationThread = new ObservationThread(observationUrls, scheduler, mainHandler);
        observationThread.start(); // Start the task
    }


    public void startProgressForecasts() {
        Log.d("MyTag", "Starting forecasts progress");
        // Create a new ForecastThread object with dependencies
        ForecastThread forecastThread = new ForecastThread(forecastUrls, scheduler, mainHandler);
        forecastThread.start(); // Start the forecastThread
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10)); // Zoom
    }


    public class ObservationThread implements Runnable {
        private final String[] urls;
        private final ScheduledExecutorService scheduler;
        private final Handler mainHandler;
        private final ObservationsParser observationsParser = new ObservationsParser();

        public ObservationThread(String[] urls, ScheduledExecutorService scheduler, Handler mainHandler) {
            this.urls = urls;
            this.scheduler = scheduler;
            this.mainHandler = mainHandler;
        }

        public void start() {
            scheduler.execute(this);
            long delay = calculateNextRunDelay();
            scheduler.scheduleAtFixedRate(this, delay, TimeUnit.HOURS.toMillis(12), TimeUnit.MILLISECONDS);
        }

        @Override
        public void run() {
            for (String url : urls) {
                try (InputStream in = new URL(url).openConnection().getInputStream()) {
                    // Parse the data from the stream
                    List<Observations> currentObservations = observationsParser.parse(in);

                    // Convert List to CopyOnWriteArrayList for thread safety if needed
                    CopyOnWriteArrayList<Observations> threadSafeList = new CopyOnWriteArrayList<>(currentObservations);

                    // Store the parsed data into the map
                    observationsMap.put(url, threadSafeList);

                    // Now trigger any UI updates or further processing
                    mainHandler.post(() -> updateUI(threadSafeList, url));
                } catch (Exception e) {
                    Log.e("ObservationThread", "Failed to fetch or parse data", e);
                }
            }
        }


        private void updateUI(CopyOnWriteArrayList<Observations> observations, String url) {
            updateLocationViews(observations, url);
        }

        private void updateLocationViews(CopyOnWriteArrayList<Observations> observations, String url) {
            if (!observations.isEmpty()) {
                Observations observation = observations.get(0);
                updateViewsForLocation(observation, getViewMapping(url));
            }
        }

        private void updateViewsForLocation(Observations observation, TextView[] views) {
            if (views != null && views.length == 4) { // Additional null check
                views[0].setText(observation.getDay());
                views[1].setText(observation.getTemperature());
                views[2].setText(observation.getPressure());
                views[3].setText(observation.getWindSpeed());
            }
        }

        private TextView[] getViewMapping(String url) {
            return urlToTextViewsMap.get(url);
        }

        private long calculateNextRunDelay() {
            // Get the current time
            Calendar now = Calendar.getInstance();

            // Get the next 08:00
            Calendar nextMorning = (Calendar) now.clone();
            nextMorning.set(Calendar.HOUR_OF_DAY, 8);
            nextMorning.set(Calendar.MINUTE, 0);
            nextMorning.set(Calendar.SECOND, 0);

            // If it's already past 08:00, get the next day's 08:00
            if (now.after(nextMorning)) {
                nextMorning.add(Calendar.DATE, 1);
            }

            // Get the next 20:00
            Calendar nextEvening = (Calendar) now.clone();
            nextEvening.set(Calendar.HOUR_OF_DAY, 20);
            nextEvening.set(Calendar.MINUTE, 0);
            nextEvening.set(Calendar.SECOND, 0);

            // If it's already past 20:00, get the next day's 20:00
            if (now.after(nextEvening)) {
                nextEvening.add(Calendar.DATE, 1);
            }

            // Get the next 08:00 or 20:00, whichever is sooner
            Calendar nextRunTime = nextMorning.before(nextEvening) ? nextMorning : nextEvening;

            // Return the calculated delay until the next run time in milliseconds
            return nextRunTime.getTimeInMillis() - now.getTimeInMillis();
        }
    }
    private void updateDateDisplay() {
        int currentIndex = weatherViewFlipper1.getDisplayedChild();
        // Fetch the date from the observations map or similar structure.
        CopyOnWriteArrayList<Observations> currentObservations = observationsMap.get(observationUrls[currentIndex]);
        if (currentObservations != null && !currentObservations.isEmpty()) {
            Observations latestObservation = currentObservations.get(0); // Observation is at index 0
            dayDate.setText(latestObservation.getDate());
            dayDate1.setText(latestObservation.getDate());
        } else {
            dayDate.setText("");
            dayDate1.setText("");
        }
    }


    public class ForecastThread implements Runnable {
        private final String[] urls;
        private final ScheduledExecutorService scheduler;
        private final Handler mainHandler;

        public ForecastThread(String[] urls, ScheduledExecutorService scheduler, Handler mainHandler) {
            this.urls = urls;
            this.scheduler = scheduler;
            this.mainHandler = mainHandler;
        }

        public void start() {
            // Immediate run in a separate thread to avoid blocking the caller
            scheduler.execute(this);

            // Calculate delay until next 08:00 or 20:00
            long delay = calculateDelay();
            // Schedule the task to run twice daily
            scheduler.scheduleAtFixedRate(this, delay, 12, TimeUnit.HOURS);
        }

        @Override
        public void run() {
            Log.d("MyTag", "Forecast Task started");
            for (String url : urls) {
                processUrl(url);
            }
        }

        private void processUrl(String url) {
            try {
                String locationName = getLocationNameFromUrl(url);
                CopyOnWriteArrayList<ThreeDayForecast> forecasts = ForecastParser.fetchDataAndParse(url);
                forecastMap.put(locationName, forecasts); // Update forecastMap with new data
                mainHandler.post(() -> updateForecastViews(locationName, forecasts));
            } catch (Exception e) {
                Log.e("MyTag", "Error in fetching or parsing data: " + e.getMessage());
            }
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

        private long calculateDelay() {
            // Get the current time
            Calendar now = Calendar.getInstance();

            // Get the next 08:00
            Calendar nextMorning = (Calendar) now.clone();
            nextMorning.set(Calendar.HOUR_OF_DAY, 8);
            nextMorning.set(Calendar.MINUTE, 0);
            nextMorning.set(Calendar.SECOND, 0);

            // If it's already past 08:00, get the next day's 08:00
            if (now.after(nextMorning)) {
                nextMorning.add(Calendar.DATE, 1);
            }

            // Get the next 20:00
            Calendar nextEvening = (Calendar) now.clone();
            nextEvening.set(Calendar.HOUR_OF_DAY, 20);
            nextEvening.set(Calendar.MINUTE, 0);
            nextEvening.set(Calendar.SECOND, 0);

            // If it's already past 20:00, get the next day's 20:00
            if (now.after(nextEvening)) {
                nextEvening.add(Calendar.DATE, 1);
            }

            // Get the next 08:00 or 20:00, whichever is sooner
            Calendar nextRunTime = nextMorning.before(nextEvening) ? nextMorning : nextEvening;

            // Return the calculated delay until the next run time in milliseconds
            return nextRunTime.getTimeInMillis() - now.getTimeInMillis();
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
}