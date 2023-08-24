package com.sam.sambook.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookRequestModel implements Serializable {
    private UserModel userSendRequest;
    private UserModel userReceiveRequest;
    private BookModel bookModel;
    private String status;

    public BookRequestModel() {

    }

    public BookRequestModel(UserModel userSendRequest, UserModel userReceiveRequest, BookModel bookModel) {
        this.userSendRequest = userSendRequest;
        this.userReceiveRequest = userReceiveRequest;
        this.bookModel = bookModel;
        this.status = "pending";
    }

    public BookRequestModel(UserModel userSendRequest, UserModel userReceiveRequest, BookModel bookModel, String status) {
        this.userSendRequest = userSendRequest;
        this.userReceiveRequest = userReceiveRequest;
        this.bookModel = bookModel;
        this.status = status;
    }

    public UserModel getUserSendRequest() {
        return userSendRequest;
    }

    public void setUserSendRequest(UserModel userSendRequest) {
        this.userSendRequest = userSendRequest;
    }

    public UserModel getUserReceiveRequest() {
        return userReceiveRequest;
    }

    public void setUserReceiveRequest(UserModel userReceiveRequest) {
        this.userReceiveRequest = userReceiveRequest;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
