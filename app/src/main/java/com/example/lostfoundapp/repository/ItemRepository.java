package com.example.lostfoundapp.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lostfoundapp.model.Comment;
import com.example.lostfoundapp.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ItemRepository {
    private static final String TAG = "ItemRepository";
    private static final String PREFS_NAME = "com.example.lostfoundapp.prefs";
    private static final String KEY_ITEMS = "items";
    
    private static ItemRepository instance;
    private final SharedPreferences preferences;
    private final Gson gson;
    
    private ItemRepository(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public static synchronized ItemRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ItemRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    public List<Item> getAllItems() {
        String json = preferences.getString(KEY_ITEMS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        try {
            Type type = new TypeToken<List<Item>>() {}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Error loading items", e);
            return new ArrayList<>();
        }
    }
    
    public Item getItemById(String itemId) {
        if (itemId == null) {
            Log.e(TAG, "getItemById: itemId is null");
            return null;
        }
        
        List<Item> items = getAllItems();
        for (Item item : items) {
            if (itemId.equals(item.getId())) {
                return item;
            }
        }
        
        Log.e(TAG, "getItemById: No item found with ID: " + itemId);
        return null;
    }
    
    public List<Item> getItemsByStatus(Item.Status status) {
        List<Item> allItems = getAllItems();
        List<Item> filteredItems = new ArrayList<>();
        
        for (Item item : allItems) {
            if (item.getStatus() == status) {
                filteredItems.add(item);
            }
        }
        
        return filteredItems;
    }
    
    public void saveItems(List<Item> items) {
        String json = gson.toJson(items);
        preferences.edit().putString(KEY_ITEMS, json).apply();
        Log.d(TAG, "Saved " + items.size() + " items to SharedPreferences");
    }
    
    public void addItem(Item item) {
        List<Item> items = getAllItems();
        items.add(item);
        saveItems(items);
        Log.d(TAG, "Added item with ID: " + item.getId());
    }
    
    public void updateItem(Item updatedItem) {
        List<Item> items = getAllItems();
        boolean found = false;
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(updatedItem.getId())) {
                items.set(i, updatedItem);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveItems(items);
            Log.d(TAG, "Updated item with ID: " + updatedItem.getId());
        } else {
            Log.e(TAG, "Failed to update item: No item found with ID: " + updatedItem.getId());
        }
    }
    
    // Method to delete an item completely
    public boolean deleteItem(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            Log.e(TAG, "Cannot delete item: itemId is null or empty");
            return false;
        }
        
        List<Item> items = getAllItems();
        boolean found = false;
        int indexToRemove = -1;
        
        // Find the item to delete
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(itemId)) {
                indexToRemove = i;
                found = true;
                break;
            }
        }
        
        // Remove the item if found
        if (found && indexToRemove >= 0) {
            Log.d(TAG, "Deleting item at index: " + indexToRemove + " with ID: " + itemId);
            items.remove(indexToRemove);
            saveItems(items);
            Log.d(TAG, "Item deleted successfully. Remaining items: " + items.size());
            return true;
        } else {
            Log.e(TAG, "Failed to delete item: No item found with ID: " + itemId);
            return false;
        }
    }

    // Method to remove article content from an item
    public void removeArticleFromItem(String itemId) {
        Log.d(TAG, "Attempting to remove article from item with ID: " + itemId);
        
        Item item = getItemById(itemId);
        if (item != null) {
            Log.d(TAG, "Found item, removing article content");
            item.setArticleContent("");
            updateItem(item);
            Log.d(TAG, "Article content removed successfully");
        } else {
            Log.e(TAG, "Failed to remove article: No item found with ID: " + itemId);
        }
    }
    
    // Method to add a comment to an item
    public void addCommentToItem(String itemId, String commentText, String authorName) {
        Log.d(TAG, "Attempting to add comment to item with ID: " + itemId);
        
        Item item = getItemById(itemId);
        if (item != null) {
            // Create a new comment
            Comment comment = new Comment(
                UUID.randomUUID().toString(),
                commentText,
                authorName,
                new Date()
            );
            
            // Add comment to the item
            List<Comment> comments = item.getComments();
            if (comments == null) {
                comments = new ArrayList<>();
            }
            comments.add(comment);
            item.setComments(comments);
            
            // Update the item in storage
            updateItem(item);
        }
    }
}





