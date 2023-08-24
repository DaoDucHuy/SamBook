package com.sam.sambook.model;

import com.firebase.ui.auth.data.model.User;

import java.io.Serializable;
import java.util.Date;

public class ChatMessageModel implements Serializable {
    private String messageText;
    private UserModel sender;
    private UserModel receiver;
    private long messageTime;

    public ChatMessageModel(){

    }

    public ChatMessageModel(String messageText, UserModel sender) {
        this.messageText = messageText;
        this.sender = sender;

        messageTime = new Date().getTime();
    }

    public ChatMessageModel(String messageText, UserModel sender, UserModel receiver) {
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;
        this.messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
