package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        
        // Get error message from intent
        String errorMessage = getIntent().getStringExtra("error_message");
        if (errorMessage == null) {
            errorMessage = "Unknown error occurred";
        }
        
        // Display error message
        TextView tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setText(errorMessage);
        
        // Set up close button
        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());
    }
}