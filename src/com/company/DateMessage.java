package com.company;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMessage implements Serializable {
    private Date date;
    private String message;
    private SimpleDateFormat format = new SimpleDateFormat("K:mm a, z");

    public DateMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        Date date = getDate();
        String str = format.format(date);
        return message + " " + str;
    }
}