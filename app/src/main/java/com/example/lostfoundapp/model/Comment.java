package com.example.lostfoundapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {
    private String id;
    private String text;
    private String authorName;
    private Date timestamp;
    
    public Comment() {
        // Empty constructor needed for Firebase
    }
    
    public Comment(String id, String text, String authorName, Date timestamp) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        return timestamp != null ? sdf.format(timestamp) : "";
    }
}
