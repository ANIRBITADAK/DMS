package com.tux.dms.constants;

import java.util.ArrayList;
import java.util.List;

public class TicketPriorityType {

    public static final String TICKET_PRIORITY_KEY = "TICKET_PRIORITY";

    public static final String HIGH_PRIORITY_INT = "1";
    public static final String MED_PRIORITY_INT = "2";
    public static final String LOW_PRIORITY_INT = "3";


    public static final String HIGH_PRIORITY_STR = "High";
    public static final String MED_PRIORITY_STR = "Medium";
    public static final String LOW_PRIORITY_STR = "Low";

    private static List<String> ticketPriorityList;

    static {
        ticketPriorityList = new ArrayList<>();
        ticketPriorityList.add(HIGH_PRIORITY_STR);
        ticketPriorityList.add(MED_PRIORITY_STR);
        ticketPriorityList.add(LOW_PRIORITY_STR);

    }

    public static List<String> getTicketPriorityList() {
        return ticketPriorityList;
    }
    public static Integer ticketPriorityAdapter(String priorityStr) {

        switch (priorityStr) {
            case TicketPriorityType.HIGH_PRIORITY_STR:
                return 1;
            case TicketPriorityType.MED_PRIORITY_STR:
                return 2;

            case TicketPriorityType.LOW_PRIORITY_STR:
                return 3;
            default:
                return 4;
        }
    }
}
