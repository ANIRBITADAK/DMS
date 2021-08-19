package com.tux.dms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;


import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.dto.AssignTicket;
import com.tux.dms.dto.Ticket;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketDetailsFragment extends Fragment {

    CommentRecycleAdapter adapter;
    RecyclerView recyclerView;

    String[] states = {"", "ASSIGNED", "IN-PROGRESS", "RESOLVED", "CLOSED"};
    Spinner stateSpinner;

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    TextView subjectTextView;
    TextView sourceTextView;
    TextView assignedToTextView;
    TextView assignDateTextView;
    TextView assignedStateTxtView;
    EditText commentText;
    String ticketState;
    String tickId;
    Button saveTicketButton;
    Bundle ticketPropertyBundle;

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
        subjectTextView = view.findViewById(R.id.assignSubjectText);
        sourceTextView = view.findViewById(R.id.assignSourceText);
        assignedToTextView = view.findViewById(R.id.assignedToText);
        assignDateTextView = view.findViewById(R.id.assignedDateText);
        saveTicketButton = view.findViewById(R.id.ticketDetailsSaveButton);
        commentText = view.findViewById(R.id.assignCommentEditText);

        ticketPropertyBundle = this.getArguments();
        if (ticketPropertyBundle != null) {
            tickId = (String) ticketPropertyBundle.get(TicketConst.TICKET_ID_KEY);
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
                        assignDateTextView.setText(ticket.getAssignDate());
                        if (ticket.getComments() != null && ticket.getComments().size() > 0) {
                            recyclerView = (RecyclerView) view.findViewById(R.id.commentRecyclerView);
                            adapter = new CommentRecycleAdapter(ticket.getComments(), getContext());
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
        stateSpinner = view.findViewById(R.id.ticketStateSpinner);
        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, states);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(ad);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ticketState = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((commentText.getText() != null && !commentText.getText().equals("")) && (ticketState != null && !ticketState.equals(""))) {
                    AssignTicket comment = new AssignTicket();
                    if (commentText.getText() != null) {
                        comment.setCommentText(commentText.getText().toString());
                    }
                    if (ticketState != null) {
                        comment.setState(ticketState);
                    }
                    Call<Ticket> commentTicketCall = apiInterface.commentTicket(sessionCache.getToken(),
                            tickId, comment);

                    commentTicketCall.enqueue(new Callback<Ticket>() {
                        @Override
                        public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                            if (response.code() == 200) {

                                moveTheFragment(view);
                            }
                        }

                        @Override
                        public void onFailure(Call<Ticket> call, Throwable t) {

                        }
                    });

                } else {
                    moveTheFragment(view);
                }
            }
        });
        return view;
    }

    private void moveTheFragment(View view) {
        TicketTableFragment ticketTableFragment = new TicketTableFragment();
        ticketTableFragment.setArguments(ticketPropertyBundle);
        FragmentManager manager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ticketTableFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}