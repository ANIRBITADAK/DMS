package com.tux.dms;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.constants.TicketStateType;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketSearchFragment extends Fragment {


    String[] states = {"", "New", "Assigned", "In-Progress", "Resolved"};
    Spinner stateSpinner;

    String[] priorities = {"", "High", "Medium", "Low"};
    Spinner prioritySpinner;
    Button ticketSearchButton;
    EditText date;
    String subject;
    String state;
    String priority;

    ImageButton imageButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketSearchFragment newInstance(String param1, String param2) {
        TicketSearchFragment fragment = new TicketSearchFragment();
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

        View view=inflater.inflate(R.layout.fragment_search, container, false);
        imageButton=view.findViewById(R.id.calendarImgButton);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        date = view.findViewById(R.id.date);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 += 1;
                        String dateString = i2 + "-" + i1 + "-" + i;
                        date.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        stateSpinner = view.findViewById(R.id.stateSpinner);
        ArrayAdapter stateArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, states);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateArrayAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        prioritySpinner = view.findViewById(R.id.prioritySpinner);
        ArrayAdapter priorityAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ticketSearchButton = view.findViewById(R.id.ticketSearchButton);
        ticketSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TicketTableFragment ticketTableFragment = new TicketTableFragment();
                String startDate = null;
                String endDate = null;
                if (date.getText() != null && !date.getText().toString().equals("")) {
                    startDate = formatDate(date.getText().toString());
                    endDate = addDays(startDate, 5);
                }
                Bundle ticketSearchBundle = getTicketSearchBundle(subject, state, priority, startDate, endDate);
                ticketTableFragment.setArguments(ticketSearchBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ticketTableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;
    }

    private Bundle getTicketSearchBundle(String subject, String state, String priority,
                                         String startDate, String endDate) {
        Bundle ticketSearchBundle = new Bundle();
        ticketSearchBundle.putString(TicketConst.TICKET_SEARCH_FLOW_KEY, TicketConst.TICKET_SEARCH_FLOW_VALUE);
        if (subject != null) {
            ticketSearchBundle.putString(TicketConst.TICKET_SUBJECT_KEY, subject);
        }
        if (state != null && !state.equals("")) {
            ticketSearchBundle.putString(TicketStateType.TICKET_STATE_TYPE_KEY, state);
        }
        if (priority != null && !priority.equals("")) {
            ticketSearchBundle.putString(TicketPriorityType.TICKET_PRIORITY_KEY,
                    TicketPriorityType.ticketPriorityAdapter(priority).toString());
        }
        if (startDate != null && endDate != null) {

            ticketSearchBundle.putString(TicketConst.TICKET_START_DATE,
                    startDate);
            ticketSearchBundle.putString(TicketConst.TICKET_END_DATE,
                    endDate);
        }
        return ticketSearchBundle;
    }

    private String formatDate(String date) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyy");
        SimpleDateFormat outPutDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = null;
        String outPutDate = null;
        if (date != null) {
            if (date.contains("\\")) {
                formatedDate = date.replaceAll("\\\\", "-");
            }
            try {
                Date inputDate = inputDateFormat.parse(date);
                outPutDate = outPutDateFormat.format(inputDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return outPutDate;
    }

    private String addDays(String dateString, Integer numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String addedDate = null;
        try {
            Date date = dateFormat.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, numberOfDays);
            addedDate = dateFormat.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addedDate;
    }
}