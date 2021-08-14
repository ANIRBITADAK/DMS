package com.tux.dms;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketType;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriorityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriorityDetailsFragment extends Fragment {

    CardView highPriorityCard, mediumPriorityCard, lowPriorityCard;
    TextView highCountText;
    TextView medCountText;
    TextView lowCountText;
    TextView ticketTypeTexView;
    String ticketType;
    Bundle ticketTypeBundle;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PriorityDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriorityDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriorityDetailsFragment newInstance(String param1, String param2) {
        PriorityDetailsFragment fragment = new PriorityDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_priority_details, container, false);
        highPriorityCard = v.findViewById(R.id.highCardView);
        mediumPriorityCard = v.findViewById(R.id.mediumCardView);
        lowPriorityCard = v.findViewById(R.id.lowCardView);

        highCountText = v.findViewById(R.id.highCountText);
        medCountText = v.findViewById(R.id.mediumCountText);
        lowCountText = v.findViewById(R.id.lowCountText);

        ticketTypeBundle = this.getArguments();
        if (ticketTypeBundle != null) {
            ticketType = (String) ticketTypeBundle.get(TicketType.TICKET_TYPE_KEY);
        }
        ticketTypeTexView = v.findViewById(R.id.ticketTypeTextView);
        switch (ticketType) {
            case TicketType.ASSIGNED_TICKET:
                ticketTypeTexView.setText("Assigned Ticket");
                break;
            case TicketType.IN_PROGRESS_TICKET:
                ticketTypeTexView.setText("In Progress Ticket");
                break;
            case TicketType.RESOLVED_TICKET:
                ticketTypeTexView.setText("Resolved Ticket");
                break;
        }

        Call<TicketCount> call = apiInterface.getTicketCount(sessionCache.getToken(), ticketType);

        call.enqueue(new Callback<TicketCount>() {

            @Override
            public void onResponse(Call<TicketCount> call, Response<TicketCount> response) {

                if (response != null && response.body() != null) {
                    Integer highTicketCount = response.body().getHigh();
                    if (highTicketCount != null) {
                        highCountText.setText(String.valueOf(highTicketCount));
                    }
                    Integer medTicketCount = response.body().getMed();
                    if (medTicketCount != null) {
                        medCountText.setText(String.valueOf(medTicketCount));
                    }
                    Integer lowTicketCount = response.body().getLow();
                    if (lowTicketCount != null) {
                        lowCountText.setText(String.valueOf(lowTicketCount));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketCount> call, Throwable t) {

            }
        });

        highPriorityCard.setOnClickListener(new View.OnClickListener() {

            Call<TicketCount> call = apiInterface.getTicketCount(sessionCache.getToken(), null);

            @Override
            public void onClick(View view) {

                View v = inflater.inflate(R.layout.fragment_assign_ticket, container, false);
                TableFragment tableFragment = new TableFragment();
                ticketTypeBundle.putString(TicketPriorityType.TICKET_PRIORITY_KEY, TicketPriorityType.HIGH_PRIORITY);
                tableFragment.setArguments(ticketTypeBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, tableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        mediumPriorityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),TableFragment.class);
                //startActivity(intent);
                View v = inflater.inflate(R.layout.fragment_assign_ticket, container, false);
                TableFragment tableFragment = new TableFragment();
                ticketTypeBundle.putString(TicketPriorityType.TICKET_PRIORITY_KEY, TicketPriorityType.MED_PRIORITY);
                tableFragment.setArguments(ticketTypeBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, tableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lowPriorityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),TableFragment.class);
                //startActivity(intent);
                View v = inflater.inflate(R.layout.fragment_assign_ticket, container, false);
                TableFragment tableFragment = new TableFragment();
                ticketTypeBundle.putString(TicketPriorityType.TICKET_PRIORITY_KEY, TicketPriorityType.LOW_PRIORITY);
                tableFragment.setArguments(ticketTypeBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, tableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;
    }
}