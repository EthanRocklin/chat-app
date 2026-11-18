package com.example.chat_app.modules;

import java.util.Date;

public class ChatMessage {

    private Date timeStamp;
    private String data;
    private String message;
    private String senderID;
    private String receiverID;


    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

}
