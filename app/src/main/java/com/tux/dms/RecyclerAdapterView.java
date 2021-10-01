package com.tux.dms;

import android.content.Context;
import android.os.Bundle;
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

import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketStateType;
import com.tux.dms.dto.Ticket;

import java.util.List;

class RecyclerAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Ticket> ticketList;
    private boolean isEdit;
    private Context context;


    public RecyclerAdapterView(List<Ticket> itemList, boolean editable) {

        ticketList = itemList;
        isEdit = editable;
    }

    public RecyclerAdapterView(List<Ticket> itemList) {

        ticketList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (ticketList.get(position) == null) {
            return 1;
        } else {
            return 0;
        }
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
        viewHolder.progressBar.setVisibility(viewHolder.progressBar.VISIBLE);
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        if (ticketList != null) {
            Ticket ticket = ticketList.get(position);
            if (ticket != null) {
                if (ticket.getDocketId() != null) {
                    viewHolder.textViewDocketId.setText(ticket.getDocketId());
                }
                if (ticket.getSubject() != null) {
                    viewHolder.textViewSubject.setText(ticket.getSubject());
                }
                if (ticket.getSource() != null) {
                    viewHolder.textViewSource.setText(ticket.getSource());
                }

                viewHolder.cardViewTicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(view.getContext(), "Position:" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                        context = view.getContext();
                        if (TicketStateType.NEW_TICKET.equalsIgnoreCase(ticket.getState()) && !isEdit) {
                            Bundle ticketDetailsBundle = new Bundle();
                            TicketAssignmentFragment ticketAssignmentFragment = new TicketAssignmentFragment();
                            ticketDetailsBundle.putString(TicketConst.TICKET_ID_KEY, ticket.get_id());
                            ticketDetailsBundle.putString(TicketConst.TICKET_DOCKET_ID_KEY, ticket.getDocketId());
                            ticketDetailsBundle.putString(TicketConst.TICKET_SUBJECT_KEY, ticket.getSubject());
                            ticketDetailsBundle.putString(TicketConst.TICKET_SOURCE_KEY, ticket.getSource());
                            //ticketDetailsBundle.putString(TicketConst.TICKET_IMG_PATH, ticket.getFilePath());
                            ticketAssignmentFragment.setArguments(ticketDetailsBundle);
                            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, ticketAssignmentFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if (TicketStateType.NEW_TICKET.equalsIgnoreCase(ticket.getState()) && isEdit) {
                            // show a fragment for show edit of tickets
                        } else {
                            Bundle ticketBundle = new Bundle();
                            ticketBundle.putString(TicketConst.TICKET_ID_KEY, ticket.get_id());
                            ticketBundle.putString(TicketConst.TICKET_DOCKET_ID_KEY, ticket.getDocketId());
                            ticketBundle.putString(TicketConst.TICKET_SUBJECT_KEY, ticket.getSubject());
                            ticketBundle.putString(TicketStateType.TICKET_STATE_TYPE_KEY, ticket.getState());
                            ticketBundle.putString(TicketPriorityType.TICKET_PRIORITY_KEY, ticket.getPriority());
                            //ticketBundle.putString(TicketConst.TICKET_IMG_PATH, ticket.getFilePath());
                            TicketDetailsFragment ticketDetailsFragment = new TicketDetailsFragment();
                            ticketDetailsFragment.setArguments(ticketBundle);
                            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, ticketDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                });
            }
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDocketId;
        TextView textViewSource;
        TextView textViewSubject;
        CardView cardViewTicket;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDocketId = itemView.findViewById(R.id.assignDocketIdTextView);
            textViewSubject = itemView.findViewById(R.id.assignSubjectText);
            textViewSource = itemView.findViewById(R.id.assignSourceText);
            cardViewTicket = itemView.findViewById(R.id.ticketCardView);
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
