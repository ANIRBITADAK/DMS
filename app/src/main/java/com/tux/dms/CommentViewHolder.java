package com.tux.dms;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView comment;
    TextView date;
    View view;

    CommentViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.nameTextView);
        date = (TextView) itemView.findViewById(R.id.dateTextView);
        comment = (TextView) itemView.findViewById(R.id.commentTextView);
        view = itemView;
    }

}
