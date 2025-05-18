package com.example.lostfoundapp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lostfoundapp.adapter.CommentAdapter;
import com.example.lostfoundapp.model.Comment;
import com.example.lostfoundapp.model.Item;
import com.example.lostfoundapp.repository.ItemRepository;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailActivity";
    
    // UI components
    private ImageView ivItemImage;
    private TextView tvStatus, tvTitle, tvCategory, tvDate, tvLocation;
    private TextView tvBuilding, tvRoom, tvCampus, tvDescription, tvContact;
    private LinearLayout layoutArticleSection, layoutCommentsSection;
    private TextView tvArticle;
    private Button btnViewFullArticle, btnAddComment, btnContactReporter, btnClaimItem;
    private RecyclerView rvComments;
    
    // Data
    private String itemId;
    private Item item;
    private ItemRepository repository;
    private CommentAdapter commentAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Item Details");
        }
        
        // Initialize repository
        repository = ItemRepository.getInstance(this);
        
        // Initialize views
        initializeViews();
        
        // Get item ID from intent
        itemId = getIntent().getStringExtra("item_id");
        if (itemId == null) {
            Log.e(TAG, "No item ID provided");
            Toast.makeText(this, "Error: Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        Log.d(TAG, "Received item ID: " + itemId);
        
        // Load item details
        loadItemDetails();
        
        // Set up button listeners
        setupButtonListeners();
    }
    
    private void initializeViews() {
        ivItemImage = findViewById(R.id.ivItemDetailImage);
        tvStatus = findViewById(R.id.tvItemDetailStatus);
        tvTitle = findViewById(R.id.tvItemDetailTitle);
        tvCategory = findViewById(R.id.tvItemDetailCategory);
        tvDate = findViewById(R.id.tvItemDetailDate);
        tvLocation = findViewById(R.id.tvItemDetailLocation);
        tvBuilding = findViewById(R.id.tvItemDetailBuilding);
        tvRoom = findViewById(R.id.tvItemDetailRoom);
        tvCampus = findViewById(R.id.tvItemDetailCampus);
        tvDescription = findViewById(R.id.tvItemDetailDescription);
        tvContact = findViewById(R.id.tvItemDetailContact);
        layoutArticleSection = findViewById(R.id.layoutArticleSection);
        tvArticle = findViewById(R.id.tvItemDetailArticle);
        btnViewFullArticle = findViewById(R.id.btnViewFullArticle);
        layoutCommentsSection = findViewById(R.id.layoutCommentsSection);
        rvComments = findViewById(R.id.rvItemDetailComments);
        btnAddComment = findViewById(R.id.btnAddComment);
        btnContactReporter = findViewById(R.id.btnContactReporter);
        btnClaimItem = findViewById(R.id.btnClaimItem);
        
        // Set up RecyclerView for comments
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(); // Remove the context parameter
        rvComments.setAdapter(commentAdapter);
    }
    
    private void loadItemDetails() {
        // Get item from repository
        item = repository.getItemById(itemId);
        if (item == null) {
            Log.e(TAG, "Item not found with ID: " + itemId);
            Toast.makeText(this, "Error: Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Set item details to views
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        
        // Set category
        if (item.getCategory() != null && !item.getCategory().isEmpty()) {
            tvCategory.setText(item.getCategory());
            tvCategory.setVisibility(View.VISIBLE);
        } else {
            tvCategory.setVisibility(View.GONE);
        }
        
        // Set status with appropriate color
        if (item.getStatus() == Item.Status.LOST) {
            tvStatus.setText("LOST");
            tvStatus.setBackgroundResource(R.color.colorLost);
        } else if (item.getStatus() == Item.Status.FOUND) {
            tvStatus.setText("FOUND");
            tvStatus.setBackgroundResource(R.color.colorFound);
        } else {
            tvStatus.setText("CLAIMED");
            tvStatus.setBackgroundResource(R.color.colorClaimed);
        }
        
        // Set date and location
        tvDate.setText("Date: " + item.getFormattedDate());
        tvLocation.setText("Location: " + item.getLocation());
        
        // Set building details
        if (item.getBuildingName() != null && !item.getBuildingName().isEmpty()) {
            tvBuilding.setText("Building: " + item.getBuildingName());
            tvBuilding.setVisibility(View.VISIBLE);
        } else {
            tvBuilding.setVisibility(View.GONE);
        }
        
        if (item.getRoomNumber() != null && !item.getRoomNumber().isEmpty()) {
            tvRoom.setText("Room: " + item.getRoomNumber());
            tvRoom.setVisibility(View.VISIBLE);
        } else {
            tvRoom.setVisibility(View.GONE);
        }
        
        if (item.getCampusArea() != null && !item.getCampusArea().isEmpty()) {
            tvCampus.setText("Campus Area: " + item.getCampusArea());
            tvCampus.setVisibility(View.VISIBLE);
        } else {
            tvCampus.setVisibility(View.GONE);
        }
        
        // Set contact information
        if (item.getContactEmail() != null && !item.getContactEmail().isEmpty()) {
            tvContact.setText("Email: " + item.getContactEmail());
            tvContact.setVisibility(View.VISIBLE);
        } else {
            tvContact.setVisibility(View.GONE);
        }

        // Add mobile contact display
        TextView tvMobileContact = findViewById(R.id.tvItemDetailMobileContact);
        if (item.getContactMobile() != null && !item.getContactMobile().isEmpty()) {
            tvMobileContact.setText("Mobile: " + item.getContactMobile());
            tvMobileContact.setVisibility(View.VISIBLE);
        } else {
            tvMobileContact.setVisibility(View.GONE);
        }
        
        // Load image with Glide
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(this)
                .load(item.getImageUrl())
                .placeholder(R.drawable.lf_logo)
                .error(R.drawable.lf_logo)
                .into(ivItemImage);
        } else {
            ivItemImage.setImageResource(R.drawable.lf_logo);
        }
        
        // Set article content if available
        if (item.getArticleContent() != null && !item.getArticleContent().isEmpty()) {
            layoutArticleSection.setVisibility(View.VISIBLE);
            
            // Show preview of article (first 100 characters)
            String articlePreview = item.getArticleContent();
            if (articlePreview.length() > 100) {
                articlePreview = articlePreview.substring(0, 100) + "...";
            }
            tvArticle.setText(articlePreview);
        } else {
            layoutArticleSection.setVisibility(View.GONE);
        }
        
        // Load comments
        loadComments();
    }
    
    private void loadComments() {
        List<Comment> comments = item.getComments();
        if (comments != null && !comments.isEmpty()) {
            commentAdapter.updateComments(comments); // Use updateComments instead of setComments
            layoutCommentsSection.setVisibility(View.VISIBLE);
        } else {
            layoutCommentsSection.setVisibility(View.GONE);
        }
    }
    
    private void setupButtonListeners() {
        // View full article button
        btnViewFullArticle.setOnClickListener(v -> {
            if (item != null && item.getArticleContent() != null) {
                Intent intent = new Intent(ItemDetailActivity.this, ArticleActivity.class);
                intent.putExtra("item_id", itemId);
                startActivity(intent);
            }
        });
        
        // Add comment button
        btnAddComment.setOnClickListener(v -> {
            // Show dialog to add comment
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Comment");
            
            // Create a layout for the dialog
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(20, 10, 20, 10);
            
            // Add name field
            final TextView nameLabel = new TextView(this);
            nameLabel.setText("Your Name (optional):");
            layout.addView(nameLabel);
            
            final EditText nameInput = new EditText(this);
            nameInput.setHint("Anonymous");
            layout.addView(nameInput);
            
            // Add comment field
            final TextView commentLabel = new TextView(this);
            commentLabel.setText("Comment:");
            commentLabel.setPadding(0, 20, 0, 0);
            layout.addView(commentLabel);
            
            final EditText commentInput = new EditText(this);
            commentInput.setHint("Enter your comment here");
            commentInput.setMinLines(3);
            commentInput.setGravity(Gravity.TOP | Gravity.START);
            layout.addView(commentInput);
            
            builder.setView(layout);
            
            // Set up the buttons
            builder.setPositiveButton("Submit", (dialog, which) -> {
                String name = nameInput.getText().toString();
                String comment = commentInput.getText().toString();
                
                if (comment.isEmpty()) {
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Use provided name or default to "Anonymous"
                String authorName = name.isEmpty() ? "Anonymous" : name;
                
                // Add comment to database
                repository.addCommentToItem(itemId, comment, authorName);
                Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show();
                
                // Reload item and comments
                item = repository.getItemById(itemId);
                loadComments();
                
                // Show comments section
                layoutCommentsSection.setVisibility(View.VISIBLE);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            
            builder.show();
        });
        
        // Contact reporter button
        btnContactReporter.setOnClickListener(v -> {
            if (item != null) {
                // Create options dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Contact Reporter");
                
                List<String> options = new ArrayList<>();
                final boolean hasEmail = item.getContactEmail() != null && !item.getContactEmail().isEmpty();
                final boolean hasPhone = item.getContactMobile() != null && !item.getContactMobile().isEmpty();
                
                if (hasEmail) options.add("Email");
                if (hasPhone) options.add("Call");
                if (hasPhone) options.add("SMS");
                options.add("Cancel");
                
                builder.setItems(options.toArray(new String[0]), (dialog, which) -> {
                    String selected = options.get(which);
                    
                    if ("Email".equals(selected) && hasEmail) {
                        // Create email intent
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + item.getContactEmail()));
                        intent.putExtra(Intent.EXTRA_SUBJECT, 
                                "Regarding " + (item.getStatus() == Item.Status.LOST ? "lost" : "found") + 
                                " item: " + item.getTitle());
                        
                        // Try to start email app
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("Call".equals(selected) && hasPhone) {
                        // Create call intent
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + item.getContactMobile()));
                        
                        // Try to start phone app
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("SMS".equals(selected) && hasPhone) {
                        // Create SMS intent
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + item.getContactMobile()));
                        intent.putExtra("sms_body", "Regarding " + 
                                (item.getStatus() == Item.Status.LOST ? "lost" : "found") + 
                                " item: " + item.getTitle());
                        
                        // Try to start SMS app
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                
                builder.show();
            } else {
                Toast.makeText(this, "No contact information provided", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Claim item button
        btnClaimItem.setOnClickListener(v -> {
            if (item != null) {
                // Show confirmation dialog
                new AlertDialog.Builder(this)
                    .setTitle("Claim Item")
                    .setMessage("Are you sure you want to claim this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Update item status to claimed
                        item.setStatus(Item.Status.CLAIMED);
                        repository.updateItem(item);
                        
                        // Show success message
                        Toast.makeText(this, "Item claimed successfully", Toast.LENGTH_SHORT).show();
                        
                        // Refresh UI
                        loadItemDetails();
                        
                        // Set result to refresh main activity
                        setResult(RESULT_OK);
                    })
                    .setNegativeButton("No", null)
                    .show();
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh item details when returning to the activity
        if (itemId != null) {
            loadItemDetails();
        }
    }
}



