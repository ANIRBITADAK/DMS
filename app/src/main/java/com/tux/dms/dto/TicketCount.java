package com.tux.dms.dto;

public class TicketCount {

    private Integer newTicket;
    private Integer assignedTicket;
    private Integer inprogressTicket;
    private Integer resolvedTicket;
    private Integer high;
    private Integer med;
    private Integer low;

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getMed() {
        return med;
    }

    public void setMed(Integer med) {
        this.med = med;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getNewTicket() {
        return newTicket;
    }

    public void setNewTicket(Integer newTicket) {
        this.newTicket = newTicket;
    }

    public Integer getAssignedTicket() {
        return assignedTicket;
    }

    public void setAssignedTicket(Integer assignedTicket) {
        this.assignedTicket = assignedTicket;
    }

    public Integer getInprogressTicket() {
        return inprogressTicket;
    }

    public void setInprogressTicket(Integer inprogressTicket) {
        this.inprogressTicket = inprogressTicket;
    }

    public Integer getResolvedTicket() {
        return resolvedTicket;
    }

    public void setResolvedTicket(Integer resolvedTicket) {
        this.resolvedTicket = resolvedTicket;
    }
}
