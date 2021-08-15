package com.tux.dms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.tux.dms.dto.Ticket;

import java.util.List;

class RecyclerAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Ticket> ticketList;
    private Context context;


    public RecyclerAdapterView(List<Ticket> itemList) {

        ticketList = itemList;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_row, parent, false);
//            cardView = view.findViewById(R.id.cardView);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    TicketAssignFragment ticketAssignFragment = new TicketAssignFragment();
////                    FragmentManager fragmentManager = ticketAssignFragment.getFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    fragmentTransaction.replace(R.id.fragment_container, ticketAssignFragment);
////                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();
//                    Intent i=new Intent(view.getContext(),TicketDetailsFragment.class);
//                }
//            });
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
            viewHolder.textViewSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Fragment activity = (Fragment) view.getContext();
                    Toast.makeText(view.getContext(), "Position:" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                    TicketDetailsFragment ticketDetailsFragment=new TicketDetailsFragment();
                    context=view.getContext();
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,ticketDetailsFragment );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();



                }
            });

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
