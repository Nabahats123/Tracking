package com.example.nabahat.tracking;

import android.widget.Spinner;

/**
 * Created by msnab on 3/29/2018.
 */

public class Notification {

    String Bus;
    String Message;
    String Sender;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

    public Notification(){}

    public Notification(String bus, String message, String sender, String date) {
        Bus = bus;
        Message = message;
        Sender = sender;
        Date = date;
    }

    public String getBus() {
        return Bus;
    }
    public void setBus(String bus) {
        Bus = bus;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public String getSender() {
        return Sender;
    }
    public void setSender(String sender) {
        Sender = sender;
    }

}
