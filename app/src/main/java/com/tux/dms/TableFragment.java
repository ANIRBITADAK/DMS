package com.tux.dms;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.RoleConsts;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketType;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketList;
import com.tux.dms.dto.User;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment {

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableFragment newInstance(String param1, String param2) {
        TableFragment fragment = new TableFragment();
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
        View v = inflater.inflate(R.layout.fragment_table, container, false);
        Bundle ticketTypeBundle = this.getArguments();
        String ticketType = TicketType.NEW_TICKET;
        String tickPriority = "1";
        if (ticketTypeBundle != null) {
            ticketType = (String) ticketTypeBundle.get(TicketType.TICKET_TYPE_KEY);
            tickPriority = (String) ticketTypeBundle.get(TicketPriorityType.TICKET_PRIORITY_KEY);
        }
        String token = sessionCache.getToken();
        User user = sessionCache.getUser();
        String assignedUserId  = null;
        // if user is admin then fetch all records - assigned id to null.
        // otherwise set user id.
        if(user!=null && !RoleConsts.ADMIN_ROLE.equalsIgnoreCase(user.getRole())){
            assignedUserId = user.get_id();
        }
        Call<TicketList> tickList = apiInterface.getTickets(token, assignedUserId, ticketType, tickPriority,
                1, 5);
        tickList.enqueue(new Callback<TicketList>() {
            @Override
            public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                System.out.println("got ticket list" + response.body());
                TicketList ticketList = response.body();
                addHeaders();
                addData(ticketList.getTickets());
            }

            @Override
            public void onFailure(Call<TicketList> call, Throwable t) {

            }
        });

        return v;
    }

    public void addHeaders() {
        TableLayout tl = getView().findViewById(R.id.table);
        TableRow tr = new TableRow(getContext());
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Sl No.", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tr.addView(getTextView(0, "Subject", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tr.addView(getTextView(0, "Source", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tl.addView(tr, getTableLayoutParams());
    }

    public void addData(List<Ticket> tickets) {
        int index = 0;
        TableLayout tl = getView().findViewById(R.id.table);
        for (int i = 0; i < tickets.size(); i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(getLayoutParams());
            //id column
            TextView idTexView = getTextView(i, Integer.toString(++index), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getContext(), R.color.white));
            idTexView.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            String rowId = (String) idTexView.getText();
                            TicketDetailsFragment ticketDetailsFragment = new TicketDetailsFragment();
                            Bundle tickIdBundle = new Bundle();
                            tickets.get(-1);
                            String ticketId = tickets.get(i).get_id();
                            //tickIdBundle.putString(TicketConst.TICKET_ID_KEY,);
                            //ticketDetailsFragment.setArguments(ticketTypeBundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, ticketDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
            );
            tableRow.addView(idTexView);

            //adding subject column and making it clickable
            TextView subjectTexView = new TextView(getContext());
            subjectTexView.setId(i);
            subjectTexView.setText(tickets.get(i).getSubject());
            subjectTexView.setTextColor(Color.BLACK);
            subjectTexView.setPadding(40, 40, 40, 40);
            subjectTexView.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            subjectTexView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            subjectTexView.setLayoutParams(getLayoutParams());
             subjectTexView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = (String) idTexView.getText();
                    TicketDetailsFragment ticketDetailsFragment = new TicketDetailsFragment();
                    Bundle tickIdBundle = new Bundle();
                    //String ticketId = tickets.get(i).get_id();
                    //tickIdBundle.putString(TicketConst.TICKET_ID_KEY,);
                    //ticketDetailsFragment.setArguments(ticketTypeBundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, ticketDetailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
            tableRow.addView(subjectTexView);
            tableRow.addView(getTextView(i, tickets.get(i).getSource(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getContext(), R.color.white)));

            tl.addView(tableRow, getTableLayoutParams());

        }


    }



    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }
    private TableLayout.LayoutParams getTableLayoutParams() {
        return new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
    }
}