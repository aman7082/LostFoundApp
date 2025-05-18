package com.example.lostfoundapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    
    public CommentAdapter() {
        this.comments = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }
    
    @Override
    public int getItemCount() {
        return comments.size();
    }
    
    public void updateComments(List<Comment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }
    
    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvText, tvDate;
        
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvCommentAuthor);
            tvText = itemView.findViewById(R.id.tvCommentText);
            tvDate = itemView.findViewById(R.id.tvCommentDate);
        }
        
        void bind(Comment comment) {
            tvAuthor.setText(comment.getAuthorName());
            tvText.setText(comment.getText());
            tvDate.setText(comment.getFormattedTimestamp());
        }
    }
}



