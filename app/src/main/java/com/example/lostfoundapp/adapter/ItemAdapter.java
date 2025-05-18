package com.example.lostfoundapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lostfoundapp.R;
import com.example.lostfoundapp.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context context;
    private List<Item> items;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
    
    public interface OnItemLongClickListener {
        boolean onItemLongClick(Item item, int position);
    }

    public ItemAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }
    
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView locationTextView;
        private TextView dateTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivItemImage);
            titleTextView = itemView.findViewById(R.id.tvItemTitle);
            descriptionTextView = itemView.findViewById(R.id.tvItemDescription);
            locationTextView = itemView.findViewById(R.id.tvItemLocation);
            dateTextView = itemView.findViewById(R.id.tvItemDate);

            // Set click listener on the entire item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(position));
                }
            });
            
            // Set long click listener for delete functionality
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                    return longClickListener.onItemLongClick(items.get(position), position);
                }
                return false;
            });
        }

        public void bind(Item item) {
            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
            locationTextView.setText(item.getLocation());
            
            // Fix: Convert Date to String using the formatted date method
            dateTextView.setText(item.getFormattedDate());
            
            // Set status badge
            TextView statusBadge = itemView.findViewById(R.id.tvItemStatus);
            if (statusBadge != null) {
                if (item.getStatus() == Item.Status.LOST) {
                    statusBadge.setText("LOST");
                    statusBadge.setBackgroundResource(R.color.colorLost);
                } else if (item.getStatus() == Item.Status.FOUND) {
                    statusBadge.setText("FOUND");
                    statusBadge.setBackgroundResource(R.color.colorFound);
                } else {
                    statusBadge.setText("CLAIMED");
                    statusBadge.setBackgroundResource(R.color.colorClaimed);
                }
            }

            // Load image with Glide
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.lf_logo)
                    .error(R.drawable.lf_logo)
                    .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.lf_logo);
            }
        }
    }
}







