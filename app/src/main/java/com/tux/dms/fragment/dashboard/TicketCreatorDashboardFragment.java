 package com.tux.dms.fragment.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tux.dms.R;
import com.tux.dms.TicketTableFragment;
import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.RoleConstants;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketStateType;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketCreatorDashboardFragment extends Fragment {

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    CardView newTicketCard;
    TextView newTicketCount;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketCreatorDashboardFragment() {
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
    public static TicketCreatorDashboardFragment newInstance(String param1, String param2) {
        TicketCreatorDashboardFragment fragment = new TicketCreatorDashboardFragment();
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
        View v = inflater.inflate(R.layout.fragment_ticket_creator_dashboard, container, false);
        newTicketCard = v.findViewById(R.id.ticketCreatorNewTicketsCardView);

        newTicketCount = v.findViewById(R.id.ticketCreatorNewTicketCount);

        Call<TicketCount> call = apiInterface.getTicketCount(sessionCache.getToken(), "NEW");


        call.enqueue(new Callback<TicketCount>() {

            @Override
            public void onResponse(Call<TicketCount> call, Response<TicketCount> response) {

                if (response != null && response.body() != null) {

                    Integer newTickets = response.body().getNewTicket();
                    if (newTickets != null) {
                        newTicketCount.setText(String.valueOf(newTickets));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketCount> call, Throwable t) {

            }
        });

        newTicketCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = inflater.inflate(R.layout.fragment_assign_ticket, container, false);
                TicketTableFragment ticketTableFragment = new TicketTableFragment();
                Bundle ticketBundle = new Bundle();
                ticketBundle.putString(RoleConstants.ROLE_TYPE_KEY, RoleConstants.CREATOR_ROLE);
                ticketBundle.putString(TicketStateType.TICKET_STATE_TYPE_KEY, TicketStateType.NEW_TICKET);
                ticketTableFragment.setArguments(ticketBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ticketTableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return v;
    }
}