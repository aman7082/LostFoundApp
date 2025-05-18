package com.example.lostfoundapp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Item {
    public enum Status {
        LOST, FOUND, CLAIMED
    }
    
    private String id;
    private String title;
    private String description;
    private String category;
    private String location;
    private Date date;
    private Status status;
    private String imageUrl;
    private String reporterId;
    
    // University-specific fields
    private String buildingName;
    private String roomNumber;
    private String campusArea;
    private String contactEmail;
    private String contactMobile;
    
    // Additional fields
    private List<String> additionalImages;
    private String articleContent;
    private boolean hasArticle;
    
    // Comments field
    private List<Comment> comments;
    
    public Item() {
        // Empty constructor needed for Firebase
        this.id = UUID.randomUUID().toString();
        this.comments = new ArrayList<>();
        this.additionalImages = new ArrayList<>();
    }
    
    public Item(String title, String description, String category, String location, 
                Date date, Status status, String reporterId) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
        this.status = status;
        this.reporterId = reporterId;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }
    
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getCampusArea() { return campusArea; }
    public void setCampusArea(String campusArea) { this.campusArea = campusArea; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile; }
    
    public List<String> getAdditionalImages() { return additionalImages; }
    public void setAdditionalImages(List<String> additionalImages) { this.additionalImages = additionalImages; }
    
    public String getArticleContent() { return articleContent; }
    public void setArticleContent(String articleContent) { 
        this.articleContent = articleContent;
        this.hasArticle = articleContent != null && !articleContent.isEmpty();
    }
    
    public boolean hasArticle() { return hasArticle; }
    
    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }
    
    public void setComments(List<Comment> comments) { this.comments = comments; }
    
    // Formatted date methods
    public String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        return date != null ? sdf.format(date) : "";
    }
    
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return date != null ? sdf.format(date) : "";
    }
}










