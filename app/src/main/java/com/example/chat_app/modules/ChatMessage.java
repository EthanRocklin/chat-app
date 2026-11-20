package com.example.chat_app.modules;

import java.util.Date;

public class ChatMessage {

    private Date dateObject;
    private String data;
    private String message;
    private String senderID;
    private String receiverID;
    private String timeStamp;


    public String getData() {
        return data;
    }

    public Date getDateObject() {return dateObject;}

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setDateObject(Date dateObject) {this.dateObject = dateObject;}

    public void setMessage(String message) {this.message = message;}

    public void setReceiverID(String receiverID) {this.receiverID = receiverID;}

    public void setSenderID(String senderID) {this.senderID = senderID;}

    public void setTimeStamp(String timeStamp) {this.timeStamp = timeStamp;}

}
