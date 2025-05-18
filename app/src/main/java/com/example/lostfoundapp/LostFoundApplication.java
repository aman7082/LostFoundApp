package com.example.lostfoundapp;

import android.app.Application;
import android.content.Intent;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class LostFoundApplication extends Application {
    private static final String TAG = "LostFoundApplication";
    
    // Add screen density tracking
    private float screenDensity;
    private int screenWidthPx;
    private int screenHeightPx;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Get screen metrics for responsive layouts
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        
        screenDensity = displayMetrics.density;
        screenWidthPx = displayMetrics.widthPixels;
        screenHeightPx = displayMetrics.heightPixels;
        
        Log.d(TAG, "Screen density: " + screenDensity);
        Log.d(TAG, "Screen width: " + screenWidthPx + "px");
        Log.d(TAG, "Screen height: " + screenHeightPx + "px");
        
        // Set up global exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e(TAG, "Uncaught exception", throwable);
            
            try {
                // Log the error
                Log.e(TAG, "Fatal error: " + throwable.getMessage(), throwable);
                
                // Show error activity
                Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                intent.putExtra("error_message", throwable.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error handling uncaught exception", e);
            }
            
            // Kill the process
            Process.killProcess(Process.myPid());
            System.exit(1);
        });
    }
    
    // Getters for screen metrics
    public float getScreenDensity() {
        return screenDensity;
    }
    
    public int getScreenWidthPx() {
        return screenWidthPx;
    }
    
    public int getScreenHeightPx() {
        return screenHeightPx;
    }
}
