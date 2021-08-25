package com.tux.dms.fragment.dashboard;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tux.dms.R;
import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketStateType;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardFragment extends Fragment {

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    CardView assignedCard, inProgressCard, resolvedCard;
    TextView assignedTicketCount;
    TextView inProgressTicketCount;
    TextView resolvedTicketCount;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResolveTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDashboardFragment newInstance(String param1, String param2) {
        UserDashboardFragment fragment = new UserDashboardFragment();
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
        View v = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        assignedCard = v.findViewById(R.id.assignedTicketsCardView);
        inProgressCard = v.findViewById(R.id.inProgressCardView);
        resolvedCard = v.findViewById(R.id.resolvedTicketsCardView);

        assignedTicketCount = v.findViewById(R.id.assignedCountTicketText);
        inProgressTicketCount = v.findViewById(R.id.inProgressCountTicketText);
        resolvedTicketCount = v.findViewById(R.id.resolvedCountTicketText);


        Call<TicketCount> call = apiInterface.getTicketCount(sessionCache.getToken(), null);


        call.enqueue(new Callback<TicketCount>() {

            @Override
            public void onResponse(Call<TicketCount> call, Response<TicketCount> response) {

                if (response != null && response.body() != null) {

                    Integer assignTickets = response.body().getAssignedTicket();
                    if (assignTickets != null) {
                        assignedTicketCount.setText(String.valueOf(assignTickets));
                    }
                    Integer inProgressTickets = response.body().getInprogressTicket();
                    if (inProgressTickets != null) {
                        inProgressTicketCount.setText(String.valueOf(inProgressTickets));
                    }
                    Integer resolvedTickets = response.body().getResolvedTicket();
                    if (resolvedTickets != null) {
                        resolvedTicketCount.setText(String.valueOf(resolvedTickets));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketCount> call, Throwable t) {

            }
        });

        assignedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
                Bundle ticketType = new Bundle();
                ticketType.putString(TicketStateType.TICKET_STATE_TYPE_KEY, TicketStateType.ASSIGNED_TICKET); // Put anything what you want
                priorityDetailsFragment.setArguments(ticketType);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, priorityDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        inProgressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
                Bundle ticketType = new Bundle();
                ticketType.putString(TicketStateType.TICKET_STATE_TYPE_KEY, TicketStateType.IN_PROGRESS_TICKET);
                priorityDetailsFragment.setArguments(ticketType);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, priorityDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        resolvedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
                Bundle ticketType = new Bundle();
                ticketType.putString(TicketStateType.TICKET_STATE_TYPE_KEY, TicketStateType.RESOLVED_TICKET);
                priorityDetailsFragment.setArguments(ticketType);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, priorityDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
}