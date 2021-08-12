package com.tux.dms;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    CardView newCard,assignedCard,inProgressCard,resolvedCard;
    TextView newTicketCount ;
    TextView assignTicketCount ;
    TextView inProgressTicketCount;
    TextView resolvedTicketCount;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        newCard = v.findViewById(R.id.newTickets);
        assignedCard = v.findViewById(R.id.assigned);
        inProgressCard = v.findViewById(R.id.inProgress);
        resolvedCard = v.findViewById(R.id.resolved);

        newTicketCount = v.findViewById(R.id.newCountTicketText);
        assignTicketCount = v.findViewById(R.id.assignedTicketCountText);
        inProgressTicketCount = v.findViewById(R.id.inprogressTicketCountText);
        resolvedTicketCount = v.findViewById(R.id.resolvedTicketCountText);

        Call<TicketCount> call = apiInterface.getTicketCount(sessionCache.getToken(), null);


        call.enqueue(new Callback<TicketCount>() {

            @Override
            public void onResponse(Call<TicketCount> call, Response<TicketCount> response) {
                Integer newTickets = response.body().getNewTicket();
                if (newTickets != null) {
                    newTicketCount.setText(String.valueOf(newTickets));
                }
                Integer assignTickets = response.body().getAssignedTicket();
                if (assignTickets != null) {
                    assignTicketCount.setText(String.valueOf(assignTickets));
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

            @Override
            public void onFailure(Call<TicketCount> call, Throwable t) {

            }
        });

        newCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssignTicketFragment assignTicketFragment = new AssignTicketFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, assignTicketFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        assignedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PriorityDetails.class);
                getActivity().startActivity(i);
            }
        });

        inProgressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PriorityDetails.class);
                getActivity().startActivity(i);
            }
        });

        resolvedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PriorityDetails.class);
                getActivity().startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}