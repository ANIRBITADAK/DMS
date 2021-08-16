package com.tux.dms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tux.dms.dto.Comment;

import java.util.Collections;
import java.util.List;

class CommentRecycleAdapter extends RecyclerView.Adapter<CommentRecycleAdapter.CommentViewHolder> {

    List<Comment> commentList ;

    Context context;

    public CommentRecycleAdapter(List<Comment> commentList,
                                 Context context) {
        this.commentList = commentList;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View commentView = inflater.inflate(R.layout.comment_card, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(commentView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (commentList != null) {
            Comment comment = commentList.get(position);
            if (comment != null) {
                if (comment.getText() != null) {
                    holder.commentText.setText(comment.getText());
                }
                if (comment.getName() != null) {
                    holder.commentedBy.setText(comment.getName());
                }
                if (comment.getDate() != null) {
                    holder.commentDate.setText(comment.getDate());
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        TextView commentedBy;
        TextView commentDate;
        CardView commentCard;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentTextView);
            commentedBy = itemView.findViewById(R.id.commentedByTextView);
            commentDate = itemView.findViewById(R.id.commentDateTextView);
            commentCard = itemView.findViewById(R.id.commentCardView);
        }
    }
}
