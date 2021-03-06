package com.tux.dms;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tux.dms.constants.TicketStateType;
import com.tux.dms.fragment.dashboard.AdminDashboardFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketAssignDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketAssignDrawerFragment extends Fragment {

    boolean backTrace =false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketAssignDrawerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketAssignDrawerFragment newInstance(String param1, String param2) {
        TicketAssignDrawerFragment fragment = new TicketAssignDrawerFragment();
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

        View v= inflater.inflate(R.layout.fragment_assign_ticket, container, false);

        TicketTableFragment ticketTableFragment = new TicketTableFragment();
        AdminDashboardFragment adminDashboardFragment = new AdminDashboardFragment();

        Bundle ticketTypeBundle = new Bundle();
        ticketTypeBundle.putString(TicketStateType.TICKET_STATE_TYPE_KEY, TicketStateType.NEW_TICKET);
        ticketTableFragment.setArguments(ticketTypeBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!backTrace) {
            fragmentTransaction.replace(R.id.fragment_container, ticketTableFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            backTrace =true;
        }else{
            fragmentTransaction.replace(R.id.fragment_container, adminDashboardFragment);
            fragmentTransaction.commit();
        }
        return v;
    }
}