package com.example.nabahat.tracking;

/**
 * Created by msnab on 3/29/2018.
 */

public class Notification {

    String Bus;
    String Message;
    String Sender;

    public Notification(){}

    public Notification(String bus, String message, String sender) {
        Bus = bus;
        Message = message;
        Sender = sender;
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
