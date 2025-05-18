package com.example.lostfoundapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lostfoundapp.adapter.CommentAdapter;
import com.example.lostfoundapp.model.Comment;
import com.example.lostfoundapp.model.Item;
import com.example.lostfoundapp.repository.ItemRepository;

import java.util.List;

public class ArticleActivity extends AppCompatActivity {
    
    private static final String TAG = "ArticleActivity";
    
    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivMainImage;
    private ImageButton btnAddComment;
    private ImageButton btnDeleteArticle;
    private LinearLayout commentsSection;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private String itemId;
    private ItemRepository repository;
    
    // Define result codes
    public static final int RESULT_ARTICLE_DELETED = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        
        Log.d(TAG, "onCreate: Starting ArticleActivity");
        
        // Initialize repository
        repository = ItemRepository.getInstance(this);
        
        // Initialize views
        tvTitle = findViewById(R.id.tvArticleTitle);
        tvContent = findViewById(R.id.tvArticleContent);
        ivMainImage = findViewById(R.id.ivArticleImage);
        btnAddComment = findViewById(R.id.btnAddComment);
        btnDeleteArticle = findViewById(R.id.btnDeleteArticle);
        commentsSection = findViewById(R.id.commentsSection);
        rvComments = findViewById(R.id.rvComments);
        
        // Set up RecyclerView for comments
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        rvComments.setAdapter(commentAdapter);
        
        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String imageUrl = intent.getStringExtra("imageUrl");
            itemId = intent.getStringExtra("itemId");
            
            Log.d(TAG, "Received data - Title: " + title);
            Log.d(TAG, "Received data - Content: " + content);
            Log.d(TAG, "Received data - ItemId: " + itemId);
            
            // Set data to views
            if (title != null) tvTitle.setText(title);
            if (content != null) tvContent.setText(content);
            
            // Load image if available
            if (imageUrl != null && !imageUrl.isEmpty()) {
                ivMainImage.setVisibility(View.VISIBLE);
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(ivMainImage);
            } else {
                ivMainImage.setVisibility(View.GONE);
            }
            
            // Load comments if available
            loadComments();
        } else {
            Log.e(TAG, "Intent is null");
            finish();
            return;
        }
        
        // Set up comment button
        btnAddComment.setOnClickListener(v -> {
            Log.d(TAG, "Add comment button clicked");
            // Show dialog to add comment
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Comment");
            
            // Set up the input
            final EditText input = new EditText(this);
            builder.setView(input);
            
            // Set up the buttons
            builder.setPositiveButton("Submit", (dialog, which) -> {
                String comment = input.getText().toString();
                if (!comment.isEmpty()) {
                    // Add comment to database
                    repository.addCommentToItem(itemId, comment, "Anonymous User");
                    Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show();
                    
                    // Reload comments
                    loadComments();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            
            builder.show();
        });
        
        // Set up delete button
        btnDeleteArticle.setOnClickListener(v -> {
            Log.d(TAG, "Delete article button clicked");
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                .setTitle("Delete Article")
                .setMessage("Are you sure you want to delete this article?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Log.d(TAG, "Deleting article with ID: " + itemId);
                    // Delete article from database
                    if (itemId != null) {
                        repository.removeArticleFromItem(itemId);
                        Toast.makeText(this, "Article deleted", Toast.LENGTH_SHORT).show();
                        
                        // Set result to indicate article was deleted
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("itemId", itemId);
                        setResult(RESULT_ARTICLE_DELETED, resultIntent);
                        
                        finish(); // Go back to previous screen
                    } else {
                        Log.e(TAG, "Cannot delete article: itemId is null");
                        Toast.makeText(this, "Error: Could not delete article", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        });
    }
    
    private void loadComments() {
        if (itemId == null) {
            Log.e(TAG, "Cannot load comments: itemId is null");
            return;
        }
        
        Item item = repository.getItemById(itemId);
        if (item != null) {
            List<Comment> comments = item.getComments();
            if (comments != null && !comments.isEmpty()) {
                commentsSection.setVisibility(View.VISIBLE);
                commentAdapter.updateComments(comments);
                Log.d(TAG, "Loaded " + comments.size() + " comments");
            } else {
                commentsSection.setVisibility(View.GONE);
                Log.d(TAG, "No comments to display");
            }
        } else {
            Log.e(TAG, "Item not found with ID: " + itemId);
        }
    }
}



