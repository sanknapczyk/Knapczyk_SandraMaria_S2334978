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
/*
 * Sandra Maria Knapczyk
 * Student ID: S2334978
 * MPD CW1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    // Google Maps
    private GoogleMap googleMap;

    // UI Components: View management
    private ViewSwitcher viewSwitcher;
    private ViewFlipper weatherViewFlipper1, weatherViewFlipper2;

    // UI Components: Buttons
    private Button backButton, forButton;
    private ImageButton nextButton1, prevButton1, nextButton2, prevButton2;
    private ImageButton calendarButton, notesButton;

    // UI Components: Spinner for location selection
    private Spinner locationSpinner;

    // UI Components: Text views for displaying date information
    private TextView dayDate, dayDate1;

    // UI Components: Forecast details for various locations
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

    // UI Components: Image views for displaying weather icons in forecasts
    private ImageView glasgowIcon1, glasgowIcon2, glasgowIcon3, londonIcon1, londonIcon2, londonIcon3;
    private ImageView newYorkIcon1, newYorkIcon2, newYorkIcon3, omanIcon1, omanIcon2, omanIcon3;
    private ImageView mauritiusIcon1, mauritiusIcon2, mauritiusIcon3, bangladeshIcon1, bangladeshIcon2, bangladeshIcon3;

    // Geographical coordinates for map locations
    private final LatLng[] CITY_COORDINATES = {
            new LatLng(55.8642, -4.2518), // Glasgow
            new LatLng(51.5074, -0.1278), // London
            new LatLng(40.7128, -74.0060), // New York
            new LatLng(20.4883, 56.2402), // Oman
            new LatLng(-20.4047, 57.4117), // Mauritius
            new LatLng(24.2079, 90.2569)  // Bangladesh
    };

    // URLs for fetching weather observations
    private final String[] observationUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"
    };

    // URLs for fetching weather forecasts
    private final String[] forecastUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    // Maps for storing parsed weather data, thread-safe
    private final Map<String, CopyOnWriteArrayList<ThreeDayForecast>> forecastMap = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArrayList<Observations>> observationsMap = new ConcurrentHashMap<>();

    // Mapping buttons to their respective forecast information
    HashMap<Integer, Pair<String, Integer>> buttonToForecastInfo = new HashMap<>();
    private Map<String, TextView[]> urlToTextViewsMap;

    // Multithreading and handler for updating UI
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
        // Glasgow observation text views
        TextView glasgowDay = findViewById(R.id.glasgowDay);
        TextView glasgowTemperature = findViewById(R.id.glasgowTemperature);
        TextView glasgowPressure = findViewById(R.id.glasgowPressure);
        TextView glasgowWind = findViewById(R.id.glasgowWind);

        // London observation text views
        TextView londonDay = findViewById(R.id.londonDay);
        TextView londonTemperature = findViewById(R.id.londonTemperature);
        TextView londonPressure = findViewById(R.id.londonPressure);
        TextView londonWind = findViewById(R.id.londonWind);

        // New York observation textviews
        TextView newYorkDay = findViewById(R.id.newYorkDay);
        TextView newYorkTemperature = findViewById(R.id.newYorkTemperature);
        TextView newYorkPressure = findViewById(R.id.newYorkPressure);
        TextView newYorkWind = findViewById(R.id.newYorkWind);

        // Oman observation text views
        TextView omanDay = findViewById(R.id.omanDay);
        TextView omanTemperature = findViewById(R.id.omanTemperature);
        TextView omanPressure = findViewById(R.id.omanPressure);
        TextView omanWind = findViewById(R.id.omanWind);

        // Mauritius observation text views
        TextView mauritiusDay = findViewById(R.id.mauritiusDay);
        TextView mauritiusTemperature = findViewById(R.id.mauritiusTemperature);
        TextView mauritiusPressure = findViewById(R.id.mauritiusPressure);
        TextView mauritiusWind = findViewById(R.id.mauritiusWind);

        // Bangladesh observation text views
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

        // Initialise Text view to display the date
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
            public void onNothingSelected(AdapterView<?> parent) { // Default position
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
        // Debug log to trace which button was clicked and the associated information
        logButtonInfoForDebugging();

        // Handle button clicks based on the associated forecast or control action
        Pair<String, Integer> info = buttonToForecastInfo.get(v.getId());
        if (info != null) {
            // Handle forecast-related buttons
            handleForecastButtonClicked(info, v);
        } else {
            // Handle control buttons (navigation, external apps)
            handleControlButtonClicked(v);
        }
    }

    /**
     * Logs the ID and associated info of the clicked button for debugging.
     */
    private void logButtonInfoForDebugging() {
        for (Map.Entry<Integer, Pair<String, Integer>> entry : buttonToForecastInfo.entrySet()) {
            Log.d("MyTag", "Button ID: " + entry.getKey() + ", Info: " + entry.getValue().first + ", " + entry.getValue().second);
        }
    }

    /**
     * Handles clicks specifically on buttons associated with forecasts.
     * It checks the forecast availability and displays it if available.
     *
     * @param info The paired location name and day index info associated with the forecast button.
     * @param v The view that was clicked.
     */
    private void handleForecastButtonClicked(Pair<String, Integer> info, View v) {
        String locationName = info.first;
        Integer dayIndex = info.second;

        // Log details about the location and day index being accessed
        Log.d("MyTag", "Location: " + locationName + ", Day Index: " + dayIndex);

        // Retrieve forecast data for the specified location
        CopyOnWriteArrayList<ThreeDayForecast> forecasts = forecastMap.get(locationName);
        // Log the current state of the forecast map
        Log.d("MyTag", "ForecastMap: " + forecastMap);

        // Display the forecast details if they are available and valid
        if (forecasts != null && dayIndex < forecasts.size()) {
            showForecastDetails(locationName, dayIndex);
            Log.d("MyTag", "detailed forecast button triggered");
        } else {
            // Log a message if no forecast data is available for the specified day
            Log.d("MyTag", "No forecast available for " + locationName + " on day index " + dayIndex);
        }
    }

    /**
     * Handles click events for various control buttons within the application.
     * This method determines which control button was clicked and executes the corresponding action,
     * such as navigating between views, starting tasks, or launching other applications.
     *
     * @param v The view that was clicked, which triggers the event.
     */
    private void handleControlButtonClicked(View v) {
        if (v == forButton) {
            // Navigate to the next forecast view in the viewSwitcher
            viewSwitcher.showNext();
            // Start fetching and displaying forecasts
            startProgressForecasts();
            // Log the navigation action to the forecast screen
            Log.d("MyTag", "go to forecast screen");
        } else if (v == nextButton1 || v == nextButton2) {
            // Navigate to the next item in the weather views using the view flippers
            navigateThroughWeatherViews(true);
        } else if (v == prevButton1 || v == prevButton2) {
            // Navigate to the previous item in the weather views
            navigateThroughWeatherViews(false);
        } else if (v == backButton) {
            // Navigate back to the initial or previous screen in the viewSwitcher
            viewSwitcher.showPrevious();
            // Restart or trigger observation data fetching processes
            startProgressObservations();
            // Log the navigation action to the start screen
            Log.d("MyTag", "back to start screen - observations");
        } else if (v == calendarButton) {
            // Launch the calendar application to insert a new event or manage schedule
            launchCalendarApp();
        } else if (v == notesButton) {
            // Launch the notes application, specifically Google Keep, to create or edit notes
            launchNotesApp();
        }
    }

    /**
     * Navigates through the weather views in the application based on the user's input.
     * It adjusts both the content of the weather view flippers and the selected item in the spinner.
     *
     * @param next If true, navigate to the next view; if false, navigate to the previous view.
     */
    private void navigateThroughWeatherViews(boolean next) {
        // Get the current position from the first view flipper
        int currentPosition = weatherViewFlipper1.getDisplayedChild();
        // Calculate the new position based on whether the navigation is forward (next) or backward (previous)
        int newPosition = next ? (currentPosition + 1) % weatherViewFlipper1.getChildCount() :
                (currentPosition - 1 + weatherViewFlipper1.getChildCount()) % weatherViewFlipper1.getChildCount();

        // Set the new position for both weather view flippers
        weatherViewFlipper1.setDisplayedChild(newPosition);
        weatherViewFlipper2.setDisplayedChild(newPosition);
        Log.d("MyTag", "view flipper screen updated");
        // Update the spinner selection to reflect the change in view
        locationSpinner.setSelection(newPosition);
        Log.d("MyTag", "Spinner updated");
        // Update the map location to sync with the current view
        updateMapLocation(newPosition);
        Log.d("MyTag", "map updated");
        // Update the date display to reflect the new position
        updateDateDisplay();
        // Log the direction of navigation
        Log.d("MyTag", next ? "next screen" : "prev screen");
    }

    /**
     * Launches the calendar application using an intent that triggers an event insertion.
     * This method is intended to provide quick access to the calendar from the app.
     */
    private void launchCalendarApp() {
        // Create an intent to insert an event in the user's calendar
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        // Set the data URI to specify that it is an event
        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
        // Start the activity with the intent
        startActivity(calendarIntent);
        Log.d("MyTag", "calendar launched");
    }

    /**
     * Launches the Google Keep application to create a new note with some pre-filled text.
     * If Google Keep is not installed, the user is informed with a toast message.
     */
    private void launchNotesApp() {
        // Create an intent to send plain text to an app
        Intent notesIntent = new Intent(Intent.ACTION_SEND);
        notesIntent.setType("text/plain");
        // Pre-fill the note's text field with "Your text here"
        notesIntent.putExtra(Intent.EXTRA_TEXT, "Your text here");
        // Specifically target Google Keep if it's installed
        notesIntent.setPackage("com.google.android.keep");
        try {
            // Attempt to start the activity with the intent
            startActivity(notesIntent);
            Log.d("MyTag", "Notes launched");
        } catch (ActivityNotFoundException e) {
            // Show a toast message if Google Keep is not installed
            Toast.makeText(this, "Google Keep is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays detailed weather forecast information in a dialog box for a specified location and day.
     * This method retrieves forecast data for a given day, builds a detailed message,
     * and presents it in a user-friendly format.
     *
     * @param location The name of the location for which the forecast is to be displayed.
     * @param dayIndex The index of the day in the forecast list (0 = first day, 1 = second day, etc.).
     */
    private void showForecastDetails(String location, int dayIndex) {
        // Retrieve forecasts for the specified location.
        CopyOnWriteArrayList<ThreeDayForecast> forecasts = forecastMap.get(location);

        // Check if forecasts are available and the dayIndex is within the range of available data.
        if (forecasts != null && dayIndex < forecasts.size()) {
            // Retrieve the specific day's forecast.
            ThreeDayForecast forecast = forecasts.get(dayIndex);

            // Build a detailed message string describing the forecast attributes.
            String details = buildDetailMessage(forecast);

            // Prepare and display an AlertDialog to show the forecast details.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(location + " Detailed Forecast for " + forecast.getDay()); // Set dialog title to include the location and date.
            builder.setMessage(details); // Set the detailed message for the forecast.
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()); // Add an "OK" button to dismiss the dialog.
            builder.show(); // Show the dialog to the user.

        } else {
            // Display a toast message if forecast data is not available or the dayIndex is out of range.
            Toast.makeText(this, "Forecast data not available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Constructs a detailed weather forecast message from a ThreeDayForecast object.
     * This method assembles various pieces of weather data into a single, formatted string,
     * making it easier to display or log the forecast.
     *
     * @param forecast The ThreeDayForecast object containing weather information.
     * @return A string containing formatted details about the weather forecast,
     *         including wind direction, wind speed, visibility, and other relevant metrics.
     */
    private String buildDetailMessage(ThreeDayForecast forecast) {
        // Log the day for which the forecast details are being built
        Log.d("WeatherDetails", "Building message for: " + forecast.getDay());

        // Initialize StringBuilder to accumulate the details
        StringBuilder details = new StringBuilder();

        // Append wind direction if available
        if (forecast.getWindDirection() != null) {
            details.append("Wind Direction: ").append(forecast.getWindDirection()).append("\n");
        }

        // Append wind speed if available
        if (forecast.getWindSpeed() != null) {
            details.append("Wind Speed: ").append(forecast.getWindSpeed()).append("\n");
        }

        // Append visibility information if available
        if (forecast.getVisibility() != null) {
            details.append("Visibility: ").append(forecast.getVisibility()).append("\n");
        }

        // Append atmospheric pressure if available
        if (forecast.getPressure() != null) {
            details.append("Pressure: ").append(forecast.getPressure()).append("\n");
        }

        // Append humidity level if available
        if (forecast.getHumidity() != null) {
            details.append("Humidity: ").append(forecast.getHumidity()).append("\n");
        }

        // Append UV risk level if available
        if (forecast.getUvRisk() != null) {
            details.append("UV Risk: ").append(forecast.getUvRisk()).append("\n");
        }

        // Append pollution level if available
        if (forecast.getPollution() != null) {
            details.append("Pollution: ").append(forecast.getPollution()).append("\n");
        }

        // Append sunset time if available
        if (forecast.getSunset() != null) {
            details.append("Sunset: ").append(forecast.getSunset()).append("\n");
        }

        // Return the complete details string
        return details.toString();
    }


    /**
     * Starts observation processes by launching a thread to fetch observation data.
     */
    public void startProgressObservations() {
        Log.d("MyTag", "Starting observations progress");
        // Create and start a thread for fetching observation data from URLs
        ObservationThread observationThread = new ObservationThread(observationUrls, scheduler, mainHandler);
        observationThread.start();
    }

    /**
     * Starts forecast processes by launching a thread to fetch forecast data.
     */
    public void startProgressForecasts() {
        Log.d("MyTag", "Starting forecasts progress");
        // Create and start a thread for fetching forecast data from URLs
        ForecastThread forecastThread = new ForecastThread(forecastUrls, scheduler, mainHandler);
        forecastThread.start();
    }

    /**
     * Callback method when the map is ready to be used. Initializes the map with a default location.
     * @param googleMap the GoogleMap instance that is ready for use
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Initially update the map location to the first location in the list
        updateMapLocation(0);
    }

    /**
     * Updates the map location based on a given position index.
     * @param position the index of the location to display on the map
     */
    private void updateMapLocation(int position) {
        // Validate map readiness and the position index
        if (googleMap == null || position < 0 || position >= CITY_COORDINATES.length) {
            Log.e("MainActivity", "Invalid map state or position.");
            return;
        }

        // Retrieve the geographical coordinates for the specified location
        LatLng selectedLocation = CITY_COORDINATES[position];

        // Clear any existing markers on the map
        googleMap.clear();

        // Add a marker at the new location and update the title with the currently selected location from spinner
        googleMap.addMarker(new MarkerOptions().position(selectedLocation).title("Marker in " + locationSpinner.getSelectedItem().toString()));

        // Move and zoom the camera to the new location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10));
        Log.d("MyTag", "map updated");
    }


    /**
     * A class that implements Runnable to fetch and update observations data asynchronously.
     */
    public class ObservationThread implements Runnable {
        // URL array to fetch observations from
        private final String[] urls;
        // Executor service for scheduling and executing tasks
        private final ScheduledExecutorService scheduler;
        // Handler for posting tasks in the UI thread
        private final Handler mainHandler;
        // Parser for converting fetched data into Observation objects
        private final ObservationsParser observationsParser = new ObservationsParser();

        /**
         * Constructor for ObservationThread.
         * @param urls URLs from which to fetch observations.
         * @param scheduler Scheduler for managing task execution.
         * @param mainHandler Handler for interacting with the main thread.
         */
        public ObservationThread(String[] urls, ScheduledExecutorService scheduler, Handler mainHandler) {
            this.urls = urls;
            this.scheduler = scheduler;
            this.mainHandler = mainHandler;
        }

        /**
         * Starts the execution of the observation fetching task.
         */
        public void start() {
            // Execute the run method immediately
            scheduler.execute(this);
            Log.d("MyTag", "update at start working- observation");
            // Calculate initial delay and schedule the task at a fixed rate
            long delay = calculateNextRunDelay();
            scheduler.scheduleAtFixedRate(this, delay, TimeUnit.HOURS.toMillis(12), TimeUnit.MILLISECONDS);
            Log.d("MyTag", "update between 8am and 8pm scheduled- observation");
        }

        /**
         * Core method that fetches and processes observation data.
         */
        @Override
        public void run() {
            // Loop through each URL to fetch data
            for (String url : urls) {
                try (InputStream in = new URL(url).openConnection().getInputStream()) {
                    // Parse the input stream to extract observation data
                    List<Observations> currentObservations = observationsParser.parse(in);

                    // Use CopyOnWriteArrayList for thread-safe modifications
                    CopyOnWriteArrayList<Observations> threadSafeList = new CopyOnWriteArrayList<>(currentObservations);

                    // Store the parsed observations in a concurrent map
                    observationsMap.put(url, threadSafeList);

                    // Post a task on the main thread to update the UI
                    mainHandler.post(() -> updateUI(threadSafeList, url));
                    Log.d("MyTag", "Data parsed and observation mapping populated- observation");
                } catch (Exception e) {
                    // Log any exceptions during fetching or parsing
                    Log.e("ObservationThread", "Failed to fetch or parse data", e);

                }
            }
        }

        /**
         * Updates the UI based on the fetched observations.
         * @param observations List of observations fetched.
         * @param url URL from which the observations were fetched.
         */
        private void updateUI(CopyOnWriteArrayList<Observations> observations, String url) {
            updateLocationViews(observations, url);
            Log.d("MyTag", "UI updated- observation");
        }

        /**
         * Updates views for a specific location based on observations.
         * @param observations List of observations for the location.
         * @param url URL associated with the location.
         */
        private void updateLocationViews(CopyOnWriteArrayList<Observations> observations, String url) {
            if (!observations.isEmpty()) {
                Observations observation = observations.get(0); // Set to the first one
                updateViewsForLocation(observation, getViewMapping(url));
                Log.d("MyTag", "Views for particular location updated - observations");
            }
        }

        /**
         * Updates specific views with observation data.
         * @param observation Observation data to display.
         * @param views Array of TextViews to update.
         */
        private void updateViewsForLocation(Observations observation, TextView[] views) {
            if (views != null && views.length == 4) {
                views[0].setText(observation.getDay());
                views[1].setText(observation.getTemperature());
                views[2].setText(observation.getPressure());
                views[3].setText(observation.getWindSpeed());
                Log.d("MyTag", "Observation data populated into text views");
            }
        }

        /**
         * Maps URLs to TextView arrays for updating specific views.
         * @param url URL to map from.
         * @return An array of TextViews associated with the URL.
         */
        private TextView[] getViewMapping(String url) {
            return urlToTextViewsMap.get(url);
        }

        /**
         * Calculates the delay until the next scheduled run based on current time.
         * @return Delay in milliseconds until the next run.
         */
        private long calculateNextRunDelay() {
            Calendar now = Calendar.getInstance();

            // Determine the next occurrence of 08:00 AM and 08:00 PM
            Calendar nextMorning = (Calendar) now.clone();
            nextMorning.set(Calendar.HOUR_OF_DAY, 8);
            nextMorning.set(Calendar.MINUTE, 0);
            nextMorning.set(Calendar.SECOND, 0);
            if (now.after(nextMorning)) {
                nextMorning.add(Calendar.DATE, 1);
            }

            Calendar nextEvening = (Calendar) now.clone();
            nextEvening.set(Calendar.HOUR_OF_DAY, 20);
            nextEvening.set(Calendar.MINUTE, 0);
            nextEvening.set(Calendar.SECOND, 0);
            if (now.after(nextEvening)) {
                nextEvening.add(Calendar.DATE, 1);
            }

            // Select the sooner of the two times for the next run
            Calendar nextRunTime = nextMorning.before(nextEvening) ? nextMorning : nextEvening;
            Log.d("MyTag", "delay calculated- observation");
            return nextRunTime.getTimeInMillis() - now.getTimeInMillis();
        }
    }

    /**
     * Updates the displayed date based on the currently visible weather view.
     */
    private void updateDateDisplay() {
        int currentIndex = weatherViewFlipper1.getDisplayedChild();
        // Fetch observations for the currently displayed location
        CopyOnWriteArrayList<Observations> currentObservations = observationsMap.get(observationUrls[currentIndex]);

        if (currentObservations != null && !currentObservations.isEmpty()) {
            Observations latestObservation = currentObservations.get(0);
            dayDate.setText(latestObservation.getDate());
            dayDate1.setText(latestObservation.getDate());
        } else {
            // Clear the date display if no observations are available
            dayDate.setText("");
            dayDate1.setText("");
            Log.d("MyTag", "Data updated in text views on each screen- observation");
        }
    }

    /**
     * A class that implements Runnable to fetch and update forecast data asynchronously.
     */
    public class ForecastThread implements Runnable {
        // URLs from which to fetch the forecast data.
        private final String[] urls;
        // Scheduler to manage when the thread runs.
        private final ScheduledExecutorService scheduler;
        // Handler to post tasks to the main UI thread.
        private final Handler mainHandler;

        /**
         * Constructor for creating a ForecastThread.
         * @param urls The URLs to fetch the forecasts from.
         * @param scheduler The executor service that schedules tasks.
         * @param mainHandler The handler for interacting with the main thread.
         */
        public ForecastThread(String[] urls, ScheduledExecutorService scheduler, Handler mainHandler) {
            this.urls = urls;
            this.scheduler = scheduler;
            this.mainHandler = mainHandler;
        }

        /**
         * Starts the forecast thread by first running it immediately, then scheduling it to run twice daily.
         */
        public void start() {
            // Execute the forecast thread immediately to avoid initial delay.
            scheduler.execute(this);
            Log.d("MyTag", "Update on start - forecast");

            // Calculate the delay for the next schedule to align with 08:00 or 20:00.
            long delay = calculateDelay();
            // Schedule the task to run every 12 hours after the initial delay.
            scheduler.scheduleAtFixedRate(this, delay, TimeUnit.HOURS.toMillis(12), TimeUnit.MILLISECONDS);
            Log.d("MyTag", "scheduled update between at 8am and at 8pm- forecast");
        }

        /**
         * The entry point for the thread execution that fetches and processes forecast data.
         */
        @Override
        public void run() {
            Log.d("MyTag", "Forecast Task started");
            // Process each URL to fetch and update forecasts.
            for (String url : urls) {
                processUrl(url);
            }
        }

        /**
         * Processes a single URL to fetch and update forecast data.
         * @param url The URL to process.
         */
        private void processUrl(String url) {
            try {
                // Identify the location name based on the URL.
                String locationName = getLocationNameFromUrl(url);
                // Fetch and parse the forecast data.
                CopyOnWriteArrayList<ThreeDayForecast> forecasts = ForecastParser.fetchDataAndParse(url);
                // Update the shared forecast map with new data.
                forecastMap.put(locationName, forecasts);
                // Post a task to update the UI on the main thread.
                mainHandler.post(() -> updateForecastViews(locationName, forecasts));
            } catch (Exception e) {
                Log.e("MyTag", "Error in fetching or parsing data- forecast: " + e.getMessage());
            }
        }

        /**
         * Updates the UI to display the new forecasts.
         * @param locationName The location name associated with the forecasts.
         * @param forecasts The forecasts to display.
         */
        private void updateForecastViews(String locationName, CopyOnWriteArrayList<ThreeDayForecast> forecasts) {
            if (forecasts == null || forecasts.isEmpty()) {
                Log.e("MyTag", "Forecast data is empty for " + locationName);
                return;
            }

            switch (locationName) {
                case "Glasgow":
                    updateForecastTextViews(forecasts, glasgowDay1, glasgowMax1, glasgowMin1, glasgowIcon1, glasgowDay2, glasgowMax2, glasgowMin2, glasgowIcon2, glasgowDay3, glasgowMax3, glasgowMin3, glasgowIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for glasgow populated");
                    break;
                case "London":
                    updateForecastTextViews(forecasts, londonDay1, londonMax1, londonMin1, londonIcon1, londonDay2, londonMax2, londonMin2, londonIcon2, londonDay3, londonMax3, londonMin3, londonIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for london populated");
                    break;
                case "New York":
                    updateForecastTextViews(forecasts, newYorkDay1, newYorkMax1, newYorkMin1, newYorkIcon1, newYorkDay2, newYorkMax2, newYorkMin2, newYorkIcon2, newYorkDay3, newYorkMax3, newYorkMin3, newYorkIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for NY populated");
                    break;
                case "Oman":
                    updateForecastTextViews(forecasts, omanDay1, omanMax1, omanMin1, omanIcon1, omanDay2, omanMax2, omanMin2, omanIcon2, omanDay3, omanMax3, omanMin3, omanIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for oman populated");
                    break;
                case "Mauritius":
                    updateForecastTextViews(forecasts, mauritiusDay1, mauritiusMax1, mauritiusMin1, mauritiusIcon1, mauritiusDay2, mauritiusMax2, mauritiusMin2, mauritiusIcon2, mauritiusDay3, mauritiusMax3, mauritiusMin3, mauritiusIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for mauritius populated");
                    break;
                case "Bangladesh":
                    updateForecastTextViews(forecasts, bangladeshDay1, bangladeshMax1, bangladeshMin1, bangladeshIcon1, bangladeshDay2, bangladeshMax2, bangladeshMin2, bangladeshIcon2, bangladeshDay3, bangladeshMax3, bangladeshMin3, bangladeshIcon3);
                    Log.d("MyTag", "detailed 3-day forecast for bangladesh populated");
                    break;
                default:
                    Log.e("MyTag", "Unknown location name: " + locationName);
                    break;
            }
        }

        /**
         * Updates the text and image views to display forecast details.
         * @param forecasts The forecasts to display.
         * @param day1 The TextView for the first day.
         * @param max1 The TextView for the maximum temperature on the first day.
         * @param min1 The TextView for the minimum temperature on the first day.
         * @param icon1 The ImageView for the weather icon on the first day.
         * @param day2 The TextView for the second day.
         * @param max2 The TextView for the maximum temperature on the second day.
         * @param min2 The TextView for the minimum temperature on the second day.
         * @param icon2 The ImageView for the weather icon on the second day.
         * @param day3 The TextView for the third day.
         * @param max3 The TextView for the maximum temperature on the third day.
         * @param min3 The TextView for the minimum temperature on the third day.
         * @param icon3 The ImageView for the weather icon on the third day.
         */
        private void updateForecastTextViews(CopyOnWriteArrayList<ThreeDayForecast> forecasts,
                                             TextView day1, TextView max1, TextView min1, ImageView icon1,
                                             TextView day2, TextView max2, TextView min2, ImageView icon2,
                                             TextView day3, TextView max3, TextView min3, ImageView icon3) {
            if (forecasts.size() >= 3) {
                // First day forecast
                ThreeDayForecast forecastDay1 = forecasts.get(0);
                day1.setText(forecastDay1.getDay());
                max1.setText(forecastDay1.getMaximumTemperature());
                min1.setText(forecastDay1.getMinimumTemperature());
                icon1.setImageResource(getIconResourceId(forecastDay1.getWeatherCondition()));

                // Second day forecast
                ThreeDayForecast forecastDay2 = forecasts.get(1);
                day2.setText(forecastDay2.getDay());
                max2.setText(forecastDay2.getMaximumTemperature());
                min2.setText(forecastDay2.getMinimumTemperature());
                icon2.setImageResource(getIconResourceId(forecastDay2.getWeatherCondition()));

                // Third day forecast
                ThreeDayForecast forecastDay3 = forecasts.get(2);
                day3.setText(forecastDay3.getDay());
                max3.setText(forecastDay3.getMaximumTemperature());
                min3.setText(forecastDay3.getMinimumTemperature());
                icon3.setImageResource(getIconResourceId(forecastDay3.getWeatherCondition()));
            } else {
                Log.e("MyTag", "Not enough forecast data to update all days.");
            }
        }

        /**
         * Gets the resource ID of the icon based on the weather condition.
         * @param weatherCondition The weather condition.
         * @return The resource ID of the corresponding icon.
         */
        private int getIconResourceId(String weatherCondition) {
            // Define a switch statement for different weather conditions
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

        /**
         * Calculates the delay for the next scheduled run of the thread.
         * @return The calculated delay in milliseconds.
         */
        private long calculateDelay() {
            Calendar now = Calendar.getInstance();

            // Calculate next 08:00 AM or 08:00 PM for the scheduling
            Calendar nextMorning = (Calendar) now.clone();
            nextMorning.set(Calendar.HOUR_OF_DAY, 8);
            nextMorning.set(Calendar.MINUTE, 0);
            nextMorning.set(Calendar.SECOND, 0);
            if (now.after(nextMorning)) {
                nextMorning.add(Calendar.DATE, 1);
            }

            Calendar nextEvening = (Calendar) now.clone();
            nextEvening.set(Calendar.HOUR_OF_DAY, 20);
            nextEvening.set(Calendar.MINUTE, 0);
            nextEvening.set(Calendar.SECOND, 0);
            if (now.after(nextEvening)) {
                nextEvening.add(Calendar.DATE, 1);
            }

            // Choose the earliest next run time
            Calendar nextRunTime = nextMorning.before(nextEvening) ? nextMorning : nextEvening;
            return nextRunTime.getTimeInMillis() - now.getTimeInMillis();
        }
    }

    /**
     * Maps a forecast URL to its corresponding location name.
     * This method is used to determine which location a particular URL corresponds to,
     * allowing the rest of the application to use meaningful location names instead of URLs.
     *
     * @param url The URL from which forecast data is fetched.
     * @return The name of the location associated with the URL, or null if the URL is unrecognized.
     */
    private String getLocationNameFromUrl(String url) {
        // Check each predefined URL and return the corresponding location name.
        if (url.equals(forecastUrls[0])) return "Glasgow";    // Checks if the URL is for Glasgow's forecast
        if (url.equals(forecastUrls[1])) return "London";     // Checks if the URL is for London's forecast
        if (url.equals(forecastUrls[2])) return "New York";   // Checks if the URL is for New York's forecast
        if (url.equals(forecastUrls[3])) return "Oman";       // Checks if the URL is for Oman's forecast
        if (url.equals(forecastUrls[4])) return "Mauritius";  // Checks if the URL is for Mauritius' forecast
        if (url.equals(forecastUrls[5])) return "Bangladesh"; // Checks if the URL is for Bangladesh's forecast
        Log.d("MyTag", "forecastUrl map populated");
        // Return null if the URL does not match any of the known forecast URLs
        return null;
    }

}