package com.tux.dms;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.RoleConsts;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketStateType;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketList;
import com.tux.dms.dto.User;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketTableFragment extends Fragment {

    int MAX_PAGE = 1, PAGE_COUNT = 1;

    RecyclerView recyclerView;
    RecyclerAdapterView recyclerViewAdapter;
    List<Ticket> rowsTicketList = new ArrayList<>();

    boolean isLoading = false;

    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    String token;
    String assignedUserId;
    String ticketSubject = null;
    String ticketState = null;
    String tickPriority = null;
    String startDate = null;
    String endDate = null;
    boolean isSearch = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketTableFragment() {
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
    public static TicketTableFragment newInstance(String param1, String param2) {
        TicketTableFragment fragment = new TicketTableFragment();
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

        recyclerView = v.findViewById(R.id.recyclerView);

        Bundle ticketTypeBundle = this.getArguments();

        if (ticketTypeBundle != null) {
            ticketSubject = (String) ticketTypeBundle.get(TicketConst.TICKET_SUBJECT_KEY);
            ticketState = (String) ticketTypeBundle.get(TicketStateType.TICKET_STATE_TYPE_KEY);
            tickPriority = (String) ticketTypeBundle.get(TicketPriorityType.TICKET_PRIORITY_KEY);
            startDate = (String) ticketTypeBundle.get(TicketConst.TICKET_START_DATE);
            endDate = (String) ticketTypeBundle.get(TicketConst.TICKET_END_DATE);
            String ticketSearchFlow = (String) ticketTypeBundle.get(TicketConst.TICKET_SEARCH_FLOW_KEY);
            if (ticketSearchFlow != null) {
                isSearch = true;
            }
        }
        token = sessionCache.getToken();
        User user = sessionCache.getUser();
        assignedUserId = null;
        // if user is admin then fetch all records - assigned id to null.
        // otherwise set user id.
        if (user != null && !RoleConsts.ADMIN_ROLE.equalsIgnoreCase(user.getRole())) {
            assignedUserId = user.get_id();
        }

        if (isSearch) {

            Call<TicketList> tickList = apiInterface.searchTicket(token, ticketSubject, ticketState,
                    tickPriority, startDate, endDate, PAGE_COUNT, 5);

            tickList.enqueue(new Callback<TicketList>() {
                @Override
                public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                    System.out.println("got ticket list" + response.body());
                    TicketList ticketList = response.body();
                    rowsTicketList = ticketList.getTickets();
                    MAX_PAGE = ticketList.getTotalPages();
                    initAdapter();
                    initScrollListener();
                }

                @Override
                public void onFailure(Call<TicketList> call, Throwable t) {

                }
            });

        } else {
            Call<TicketList> tickList = apiInterface.getTickets(token, assignedUserId, ticketState, tickPriority,
                    PAGE_COUNT, 5);
            tickList.enqueue(new Callback<TicketList>() {
                @Override
                public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                    System.out.println("got ticket list" + response.body());
                    TicketList ticketList = response.body();
                    if (rowsTicketList.size() < ticketList.getTickets().size()) {
                        rowsTicketList = ticketList.getTickets();
                    }
                    MAX_PAGE = ticketList.getTotalPages();
                    initAdapter();
                    initScrollListener();
                }

                @Override
                public void onFailure(Call<TicketList> call, Throwable t) {

                }
            });
        }

        return v;
    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerAdapterView(rowsTicketList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsTicketList.size() - 1 && PAGE_COUNT < MAX_PAGE) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });

    }

    private void loadMore() {

        rowsTicketList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsTicketList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsTicketList.remove(rowsTicketList.size() - 1);
                int scrollPosition = rowsTicketList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                if (isSearch) {

                    Call<TicketList> tickList = apiInterface.searchTicket(token, ticketSubject, ticketState, tickPriority,
                            startDate, endDate, ++PAGE_COUNT, 5);

                    tickList.enqueue(new Callback<TicketList>() {
                        @Override
                        public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                            System.out.println("got ticket list" + response.body());
                            TicketList ticketList = response.body();
                            for (int i = 0; i < 5; i++) {
                                if (i < ticketList.getTickets().size() && ticketList.getTickets().get(i) != null) {
                                    rowsTicketList.add(ticketList.getTickets().get(i));
                                }
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                            isLoading = false;
                        }

                        @Override
                        public void onFailure(Call<TicketList> call, Throwable t) {

                        }
                    });
                } else {
                    Call<TicketList> tickList = apiInterface.getTickets(token, assignedUserId, ticketState, tickPriority,
                            ++PAGE_COUNT, 5);

                    tickList.enqueue(new Callback<TicketList>() {
                        @Override
                        public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                            System.out.println("got ticket list" + response.body());
                            TicketList ticketList = response.body();
                            for (int i = 0; i < 5; i++) {
                                if (i < ticketList.getTickets().size() && ticketList.getTickets().get(i) != null) {
                                    rowsTicketList.add(ticketList.getTickets().get(i));
                                }
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                            isLoading = false;
                        }

                        @Override
                        public void onFailure(Call<TicketList> call, Throwable t) {

                        }
                    });
                }
            }
        }, 2000);

    }
}