package com.tux.dms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.TicketConst;
import com.tux.dms.constants.TicketPriorityType;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.AssignTicket;
import com.tux.dms.dto.User;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketAssignmentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner userSpinner;
    List<String> priorityList = new ArrayList();
    Integer priority;
    Spinner prioritySpinner;
    Map<String, String> nameToIdMap = new HashMap<>();
    String assignedId;
    String ticketId;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    TextView assignSubjectTextView;
    TextView assignSourceTextView;
    EditText commentText;
    Button saveButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TicketAssignmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketAssignmentFragment newInstance(String param1, String param2) {
        TicketAssignmentFragment fragment = new TicketAssignmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_assign_ticket, container, false);
        Call<List<User>> allUserCall = apiInterface.getAllUser(sessionCache.getToken());
        assignSubjectTextView = view.findViewById(R.id.assignSubjectText);
        assignSourceTextView = view.findViewById(R.id.assignSourceText);
        commentText = view.findViewById(R.id.assignCommentEditText);

        Bundle ticketDetailsBundle = this.getArguments();
        if (ticketDetailsBundle != null) {
            ticketId = (String) ticketDetailsBundle.get(TicketConst.TICKET_ID_KEY);
            String subject = (String) ticketDetailsBundle.get(TicketConst.TICKET_SUBJECT_KEY);
            String source = (String) ticketDetailsBundle.get(TicketConst.TICKET_SOURCE_KEY);
            assignSubjectTextView.setText(subject);
            assignSourceTextView.setText(source);
        }


        priorityList.add(TicketPriorityType.HIGH);
        priorityList.add(TicketPriorityType.MED);
        priorityList.add(TicketPriorityType.LOW);
        prioritySpinner = view.findViewById(R.id.assignPrioritySpinner);
        prioritySpinner.setOnItemSelectedListener(this);
        ArrayAdapter priorityAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, priorityList);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);


        userSpinner = view.findViewById(R.id.assignUserSpinner);
        userSpinner.setOnItemSelectedListener(this);


        allUserCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200) {
                    List<User> userList = response.body();
                    for (User user : userList) {
                        nameToIdMap.put(user.getName(), user.get_id());
                    }

                    List<String> nameList = new ArrayList<>(nameToIdMap.keySet());

                    ArrayAdapter userAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, nameList);
                    userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userSpinner.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

        saveButton = view.findViewById(R.id.assignSaveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AssignTicket assignTicket = new AssignTicket();
                assignTicket.setPriority(priority);
                assignTicket.setAssigneeId(assignedId);
                assignTicket.setCommentText(commentText.getText().toString());
                Call<Ticket> ticketCall = apiInterface.assignTicket(sessionCache.getToken(), ticketId, assignTicket);

                ticketCall.enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Ticket assigned", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = (String) adapterView.getSelectedItem();
        if (selectedItem != null && (priorityList.contains(selectedItem))) {
            switch (selectedItem) {
                case TicketPriorityType.HIGH:
                    priority = 1;
                    break;
                case TicketPriorityType.MED:
                    priority = 2;
                    break;
                case TicketPriorityType.LOW:
                    priority = 3;
                    break;
            }

        } else {
            assignedId = nameToIdMap.get(selectedItem);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}