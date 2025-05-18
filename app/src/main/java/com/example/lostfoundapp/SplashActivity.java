package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Find views
        ImageView appLogoImageView = findViewById(R.id.ivAppLogo);
        TextView appNameTextView = findViewById(R.id.tvAppName);
        TextView universityNameBelowTextView = findViewById(R.id.tvUniversityNameBelow);
        
        // Set initial alpha to 0 (completely transparent)
        appLogoImageView.setAlpha(0f);
        appNameTextView.setAlpha(0f);
        universityNameBelowTextView.setAlpha(0f);
        
        // Animate app logo to fade in first
        appLogoImageView.animate()
                .alpha(1f)
                .setDuration(1200)
                .setListener(null);
        
        // Animate app name to fade in next
        appNameTextView.animate()
                .alpha(1f)
                .setStartDelay(600)
                .setDuration(1000)
                .setListener(null);
        
        // Animate university name below to fade in last
        universityNameBelowTextView.animate()
                .alpha(1f)
                .setStartDelay(900)
                .setDuration(800)
                .setListener(null);
        
        // Proceed to main activity after splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}







