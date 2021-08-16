package com.tux.dms.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Comment {
    private String postedBy;
    private String text;
    private String filePath;
    private String name;
    private String avatar;
    private String date;

    public static final String DATE_FORMAT = "dd-MMM-yyyy";

    public Comment(String name, String date, String text) {
        this.name = name;
        this.date = date;
        this.text = text;
    }


    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        //String dateStr = date.toString();
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("dd-MMM-yyyy");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = null;
        try {
            convertedDate = sourceFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return destFormat.format(convertedDate);

        //SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
        //date = spf.format(date);
        //android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        //return dateFormat.format("yyyy-MM-dd",date);

        //SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //return dateFormat.format(date);
        //return date;
        // return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public void setDate(String date) {
        this.date = date;
    }
}
