package com.tux.dms.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TicketCopy {
    private String _id;
    private String docketId;
    private String creator;
    private String assignedTo;
    private String office;
    private String subject;
    private String description;
    private String source;
    private String priority;
    private String state;
    private List<String> pdfFilePath;
    private List<String> imageFilePath;
    private String createDate;
    private String assignDate;
    private List<Comment> comments;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDocketId() {
        return docketId;
    }

    public void setDocketId(String docketId) {
        this.docketId = docketId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
    /* public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }*/

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<String> getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(List<String> pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public List<String> getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(List<String> imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
    public String getAssignDate() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("dd-MMM-yyyy");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = null;
        try {
            convertedDate = sourceFormat.parse(assignDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return destFormat.format(convertedDate);
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
