package com.example.lostfoundapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.lostfoundapp.model.Item;
import com.example.lostfoundapp.repository.ItemRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ReportItemActivity extends AppCompatActivity {
    private static final String TAG = "ReportItemActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int PERMISSION_REQUEST_STORAGE = 101;
    
    private EditText etTitle, etDescription, etCategory, etLocation;
    private EditText etBuildingName, etRoomNumber, etCampusArea, etContactEmail, etContactMobile;
    private RadioGroup rgStatus;
    private Button btnSelectDate, btnSubmit, btnAddImage;
    private ImageView ivItemImage;
    
    private Calendar selectedDateTime = Calendar.getInstance();
    private String selectedImagePath = "";
    private ArrayList<String> additionalImages = new ArrayList<>();
    private String articleContent = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);
        
        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Report Item");
        }
        
        // Initialize views
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etCategory = findViewById(R.id.etCategory);
        etLocation = findViewById(R.id.etLocation);
        etBuildingName = findViewById(R.id.etBuildingName);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        etCampusArea = findViewById(R.id.etCampusArea);
        etContactEmail = findViewById(R.id.etContactEmail);
        etContactMobile = findViewById(R.id.etContactMobile);
        rgStatus = findViewById(R.id.rgStatus);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnAddImage = findViewById(R.id.btnAddImage);
        ivItemImage = findViewById(R.id.ivItemImage);
        
        // Set up date picker
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        
        // Set up image picker
        btnAddImage.setOnClickListener(v -> showImagePickerOptions());
        
        // Set up submit button
        btnSubmit.setOnClickListener(v -> submitItem());
        
        // Set default date
        updateDateButtonText();
    }
    
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateButtonText();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    
    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(selectedDateTime.getTime());
        btnSelectDate.setText(dateString);
    }
    
    private void showImagePickerOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        
        String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Take Photo
                    checkCameraPermissionAndOpenCamera();
                    break;
                case 1: // Choose from Gallery
                    checkStoragePermissionAndOpenGallery();
                    break;
                case 2: // Cancel
                    dialog.dismiss();
                    break;
            }
        });
        
        builder.show();
    }
    
    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{android.Manifest.permission.CAMERA}, 
                    PERMISSION_REQUEST_CAMERA);
        } else {
            openCamera();
        }
    }
    
    private void checkStoragePermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 
                    PERMISSION_REQUEST_STORAGE);
        } else {
            openGallery();
        }
    }
    
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, 
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Handle camera image
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        // Save bitmap to file
                        selectedImagePath = saveImageToInternalStorage(imageBitmap);
                        
                        // Display the image
                        ivItemImage.setVisibility(View.VISIBLE);
                        Glide.with(this)
                            .load(imageBitmap)
                            .centerCrop()
                            .into(ivItemImage);
                    }
                }
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Handle gallery image
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        // Get bitmap from URI
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(), selectedImageUri);
                        
                        // Save bitmap to file
                        selectedImagePath = saveImageToInternalStorage(bitmap);
                        
                        // Display the image
                        ivItemImage.setVisibility(View.VISIBLE);
                        Glide.with(this)
                            .load(selectedImageUri)
                            .centerCrop()
                            .into(ivItemImage);
                    } catch (IOException e) {
                        Log.e(TAG, "Error loading image from gallery", e);
                        Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    
    private String saveImageToInternalStorage(Bitmap bitmap) {
        // Create a file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
        
        // Get the directory for the app's private pictures directory
        File directory = new File(getFilesDir(), "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Create the file
        File file = new File(directory, fileName);
        
        try {
            // Compress the bitmap and save it to the file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            
            // Return the file path
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            return "";
        }
    }
    
    private void submitItem() {
        // Get input values
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String buildingName = etBuildingName.getText().toString().trim();
        String roomNumber = etRoomNumber.getText().toString().trim();
        String campusArea = etCampusArea.getText().toString().trim();
        String contactEmail = etContactEmail.getText().toString().trim();
        String contactMobile = etContactMobile.getText().toString().trim();
        
        // Validate input
        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || buildingName.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validate mobile number if provided
        if (!contactMobile.isEmpty() && !isValidMobileNumber(contactMobile)) {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Determine status
        Item.Status status;
        if (rgStatus.getCheckedRadioButtonId() == R.id.rbLost) {
            status = Item.Status.LOST;
        } else {
            status = Item.Status.FOUND;
        }
        
        // Create item and save to database
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLocation(location);
        item.setDate(selectedDateTime.getTime());
        item.setStatus(status);
        item.setReporterId("currentUserId"); // In a real app, get from authentication
        item.setBuildingName(buildingName);
        item.setRoomNumber(roomNumber);
        item.setCampusArea(campusArea);
        item.setContactEmail(contactEmail);
        item.setContactMobile(contactMobile);
        item.setImageUrl(selectedImagePath);
        item.setAdditionalImages(additionalImages);
        item.setArticleContent(articleContent);
        
        saveItemToDatabase(item);
        
        finish();
    }
    
    private void saveItemToDatabase(Item item) {
        try {
            // Generate a unique ID
            item.setId(String.valueOf(System.currentTimeMillis()));
            
            // Save to repository
            ItemRepository.getInstance(this).addItem(item);
            
            // Show success message
            Toast.makeText(this, "Item reported successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error saving item", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to validate mobile number
    private boolean isValidMobileNumber(String mobile) {
        // Basic validation - accepts 10-digit numbers
        return mobile.matches("^[0-9]{10}$");
    }
}




