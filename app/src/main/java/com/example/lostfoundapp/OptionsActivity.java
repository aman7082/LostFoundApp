package com.example.lostfoundapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class OptionsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LostFoundPrefs";
    
    // UI elements
    private Switch switchDarkMode, switchShowImages, switchNotifications;
    private Switch switchLostItemAlerts, switchFoundItemAlerts;
    private Spinner spinnerDefaultCategory;
    private EditText etDefaultLocation, etDefaultEmail;
    private Button btnSaveOptions, btnResetOptions;
    
    // Preferences
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Options");
        }
        
        // Initialize preferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Initialize UI elements
        initializeViews();
        
        // Set up category spinner
        setupCategorySpinner();
        
        // Load saved preferences
        loadPreferences();
        
        // Set up button listeners
        setupButtonListeners();
    }
    
    private void initializeViews() {
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchShowImages = findViewById(R.id.switchShowImages);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchLostItemAlerts = findViewById(R.id.switchLostItemAlerts);
        switchFoundItemAlerts = findViewById(R.id.switchFoundItemAlerts);
        spinnerDefaultCategory = findViewById(R.id.spinnerDefaultCategory);
        etDefaultLocation = findViewById(R.id.etDefaultLocation);
        etDefaultEmail = findViewById(R.id.etDefaultEmail);
        btnSaveOptions = findViewById(R.id.btnSaveOptions);
        btnResetOptions = findViewById(R.id.btnResetOptions);
    }
    
    private void setupCategorySpinner() {
        // Create an array of categories
        String[] categories = {"All Categories", "Electronics", "Books", "Clothing", 
                              "Accessories", "Documents", "Keys", "Other"};
        
        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Apply the adapter to the spinner
        spinnerDefaultCategory.setAdapter(adapter);
    }
    
    private void loadPreferences() {
        // Load display settings
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        boolean showImages = preferences.getBoolean("show_images", true);
        
        // Load notification settings
        boolean notifications = preferences.getBoolean("notifications", true);
        boolean lostItemAlerts = preferences.getBoolean("lost_item_alerts", true);
        boolean foundItemAlerts = preferences.getBoolean("found_item_alerts", true);
        
        // Load filter settings
        String defaultCategory = preferences.getString("default_category", "All Categories");
        String defaultLocation = preferences.getString("default_location", "");
        
        // Load account settings
        String defaultEmail = preferences.getString("default_email", "");
        
        // Set values to UI elements
        switchDarkMode.setChecked(darkMode);
        switchShowImages.setChecked(showImages);
        switchNotifications.setChecked(notifications);
        switchLostItemAlerts.setChecked(lostItemAlerts);
        switchFoundItemAlerts.setChecked(foundItemAlerts);
        
        // Set spinner selection
        ArrayAdapter adapter = (ArrayAdapter) spinnerDefaultCategory.getAdapter();
        int position = adapter.getPosition(defaultCategory);
        spinnerDefaultCategory.setSelection(position >= 0 ? position : 0);
        
        etDefaultLocation.setText(defaultLocation);
        etDefaultEmail.setText(defaultEmail);
    }
    
    private void setupButtonListeners() {
        // Save button
        btnSaveOptions.setOnClickListener(v -> savePreferences());
        
        // Reset button
        btnResetOptions.setOnClickListener(v -> resetPreferences());
    }
    
    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        
        // Save display settings
        editor.putBoolean("dark_mode", switchDarkMode.isChecked());
        editor.putBoolean("show_images", switchShowImages.isChecked());
        
        // Save notification settings
        editor.putBoolean("notifications", switchNotifications.isChecked());
        editor.putBoolean("lost_item_alerts", switchLostItemAlerts.isChecked());
        editor.putBoolean("found_item_alerts", switchFoundItemAlerts.isChecked());
        
        // Save filter settings
        editor.putString("default_category", spinnerDefaultCategory.getSelectedItem().toString());
        editor.putString("default_location", etDefaultLocation.getText().toString().trim());
        
        // Save account settings
        editor.putString("default_email", etDefaultEmail.getText().toString().trim());
        
        // Apply changes
        editor.apply();
        
        // Apply dark mode if changed
        if (switchDarkMode.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        Toast.makeText(this, "Options saved", Toast.LENGTH_SHORT).show();
    }
    
    private void resetPreferences() {
        // Set default values
        switchDarkMode.setChecked(false);
        switchShowImages.setChecked(true);
        switchNotifications.setChecked(true);
        switchLostItemAlerts.setChecked(true);
        switchFoundItemAlerts.setChecked(true);
        spinnerDefaultCategory.setSelection(0); // "All Categories"
        etDefaultLocation.setText("");
        etDefaultEmail.setText("");
        
        Toast.makeText(this, "Options reset to defaults", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}