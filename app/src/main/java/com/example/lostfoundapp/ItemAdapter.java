package com.example.lostfoundapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfoundapp.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> items;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(Item item, int position);
    }

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lost_found, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        
        // Set item details
        holder.tvItemName.setText(item.getTitle());
        holder.tvItemCategory.setText(item.getCategory());
        holder.tvItemLocation.setText(item.getLocation());
        holder.tvItemDate.setText(item.getFormattedDate());
        
        // Set status with appropriate color
        if (item.getStatus() == Item.Status.LOST) {
            holder.tvStatus.setText("LOST");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLost));
        } else if (item.getStatus() == Item.Status.FOUND) {
            holder.tvStatus.setText("FOUND");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFound));
        } else {
            holder.tvStatus.setText("CLAIMED");
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorClaimed));
        }
        
        // Set item image if available
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            holder.ivItemImage.setVisibility(View.VISIBLE);
            // Use Glide to load image
            com.bumptech.glide.Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.lf_logo)
                .error(R.drawable.lf_logo)
                .into(holder.ivItemImage);
        } else {
            // Set default image based on category
            holder.ivItemImage.setImageResource(R.drawable.lf_logo);
            holder.ivItemImage.setVisibility(View.VISIBLE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item, holder.getAdapterPosition());
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                return onItemLongClickListener.onItemLongClick(item, holder.getAdapterPosition());
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvItemName, tvItemCategory, tvItemLocation, tvItemDate;
        ImageView ivItemImage;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory);
            tvItemLocation = itemView.findViewById(R.id.tvItemLocation);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
        }
    }
}





