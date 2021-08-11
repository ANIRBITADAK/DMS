package com.tux.dms.cache;

import com.tux.dms.dto.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketCache {

    List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
