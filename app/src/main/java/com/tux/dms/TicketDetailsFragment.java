package com.tux.dms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketType;
import com.tux.dms.dto.Comment;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    CommentAdapter adapter;
    RecyclerView recyclerView;

    String[] states = { "Assigned", "In-Progress","Resolved" };
    Spinner stateSpinner;

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    TextView subjectTextView;
    TextView sourceTextView;
    TextView assignedToTextView;
    TextView assignDateTextView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketDetailsFragment newInstance(String param1, String param2) {
        TicketDetailsFragment fragment = new TicketDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_details, container, false);
        subjectTextView = view.findViewById(R.id.subjectText);
        sourceTextView = view.findViewById(R.id.sourceText);
        assignedToTextView = view.findViewById(R.id.assignedToText);
        assignDateTextView = view.findViewById(R.id.assigedDateText);

        Bundle ticketIdBundle = this.getArguments();
        String tickId = "";
        if (ticketIdBundle != null) {
            tickId = (String) ticketIdBundle.get(TicketConst.TICKET_ID_KEY);
        }
        Call<Ticket> ticketCall = apiInterface.getTicket(sessionCache.getToken(), tickId);

        ticketCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.code() == 200) {
                    Ticket ticket = response.body();
                    if (ticket != null) {
                        subjectTextView.setText(ticket.getSubject());
                        sourceTextView.setText(ticket.getSource());
                        assignedToTextView.setText(ticket.getAssignedToName());
                        if (ticket.getComments() != null && ticket.getComments().size() > 0) {
                            recyclerView = (RecyclerView) view.findViewById(R.id.commentRecyclerView);

                            adapter = new CommentAdapter(ticket.getComments(), getContext());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }
                }





            }


            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });
        stateSpinner = view.findViewById(R.id.spinnerState);
        stateSpinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, states);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(ad);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}