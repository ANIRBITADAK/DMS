package com.tux.dms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tux.dms.dto.Ticket;

import java.util.List;

class RecyclerAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Ticket> ticketList;

    public RecyclerAdapterView(List<Ticket> itemList) {

        ticketList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return ticketList == null ? 0 : ticketList.size();
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }



    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        if(ticketList!=null && ticketList.get(position)!=null && ticketList.get(position).getSource()!=null){
            viewHolder.textViewSource.setText(ticketList.get(position).getSource());
        }else{
            viewHolder.textViewSource.setText("");
        }
        if(ticketList!=null && ticketList.get(position)!=null && ticketList.get(position).getSubject()!=null){
            viewHolder.textViewSubject.setText(ticketList.get(position).getSubject());
        }else{
            viewHolder.textViewSubject.setText("");
        }


    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSource;
        TextView textViewSubject;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSubject = itemView.findViewById(R.id.subjectText);
            textViewSource = itemView.findViewById(R.id.sourceText);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}
