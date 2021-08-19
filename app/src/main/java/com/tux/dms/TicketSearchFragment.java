package com.tux.dms;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketSearchFragment extends Fragment  implements AdapterView.OnItemSelectedListener{

    String[] states = { "New","Assigned","In-Progress","Resolved" };
    Spinner state;

    String[] priorityArray = {"High","Medium","Low"};
    Spinner priority;
    EditText date;

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
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        imageButton=v.findViewById(R.id.calendarImgButton);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        date = v.findViewById(R.id.date);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1+=1;
                        String dateString=i2+"/"+i1+"/"+i;
                        date.setText(dateString);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        state=v.findViewById(R.id.state);
        state.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,states);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(ad);

        priority=v.findViewById(R.id.priority);
        priority.setOnItemSelectedListener(this);

        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,priorityArray);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}