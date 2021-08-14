package com.tux.dms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tux.dms.dto.Comment;

import java.util.Collections;
import java.util.List;

class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    List<Comment> list
            = Collections.emptyList();

    Context context;

    public CommentAdapter(List<Comment> list,
                                Context context)
    {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View commentView
                = inflater
                .inflate(R.layout.comment_card,
                        parent, false);

        CommentViewHolder viewHolder = new CommentViewHolder(commentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate());
        holder.comment.setText(list.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
