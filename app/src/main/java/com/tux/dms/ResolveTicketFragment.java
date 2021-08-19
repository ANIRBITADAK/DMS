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

public class ResolveTicketFragment extends Fragment {

    CardView assignedCard,inProgressCard,resolvedCard;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResolveTicketFragment() {
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
    public static ResolveTicketFragment newInstance(String param1, String param2) {
        ResolveTicketFragment fragment = new ResolveTicketFragment();
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
        View v= inflater.inflate(R.layout.fragment_resolve_ticket, container, false);
        assignedCard=v.findViewById(R.id.assignedTicketsCardView);
        inProgressCard=v.findViewById(R.id.inProgressCardView);
        resolvedCard=v.findViewById(R.id.resolvedTicketsCardView);


        assignedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Intent i = new Intent(getActivity(), PriorityDetailsFragment.class);
//                getActivity().startActivity(i);
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
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

//                Intent i = new Intent(getActivity(), PriorityDetailsFragment.class);
//                getActivity().startActivity(i);
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
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
//                Intent i = new Intent(getActivity(), PriorityDetailsFragment.class);
//                getActivity().startActivity(i);
                PriorityDetailsFragment priorityDetailsFragment = new PriorityDetailsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, priorityDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        return  v;
    }
}