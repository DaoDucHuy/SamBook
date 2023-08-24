package com.sam.sambook.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserModel implements Serializable {
    private String username;
    private String password;
    private String name;
    private String address;
    private int imgUser;
    private List<BookModel> booksUser;
    private List<BookModel> booksUserReview;
    private List<BookRequestModel> booksUserRequest;
    private List<BookModel> booksUserRecommend;
    private List<NotifyModel> notifyModelList;

    public UserModel(){

    }

    public UserModel(String username, String name, String address, int imgUser) {
        this.username = username;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
    }

    public UserModel(String username, String address, int imgUser, List<BookModel> booksUser, List<BookModel> booksUserReview, List<BookRequestModel> booksUserRequest) {
        this.username = username;
        this.address = address;
        this.imgUser = imgUser;
        this.booksUser = booksUser;
        this.booksUserReview = booksUserReview;
        this.booksUserRequest = booksUserRequest;
    }

    //Testing for the fragment review, delete later
    public UserModel(String username) {
        this.username = username;
    }

    public UserModel(String username, String password, String address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public UserModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public UserModel(String username, String password, String name, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public UserModel(String username, String password, String name, String address, int imgUser) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
    }

    public UserModel(String username, String password, String name, String address, int imgUser, List<BookModel> booksUser, List<BookModel> booksUserReview, List<BookRequestModel> booksUserRequest) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
        this.booksUser = booksUser;
        this.booksUserReview = booksUserReview;
        this.booksUserRequest = booksUserRequest;
    }

    public UserModel(String username, String password, String name, String address, List<BookModel> booksUser) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.booksUser = booksUser;
    }

    public UserModel(String name, String address, int imgUser) {
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
    }

    public UserModel(String username, String password, String name, String address, int imgUser, List<BookModel> booksUser, List<BookModel> booksUserReview, List<BookRequestModel> booksUserRequest, List<NotifyModel> notifyModelList) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
        this.booksUser = booksUser;
        this.booksUserReview = booksUserReview;
        this.booksUserRequest = booksUserRequest;
        this.notifyModelList = notifyModelList;
    }

    public UserModel(String username, String password, String name, String address, int imgUser, List<BookModel> booksUser, List<BookModel> booksUserReview, List<BookRequestModel> booksUserRequest, List<BookModel> booksUserRecommend, List<NotifyModel> notifyModelList) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
        this.booksUser = booksUser;
        this.booksUserReview = booksUserReview;
        this.booksUserRequest = booksUserRequest;
        this.booksUserRecommend = booksUserRecommend;
        this.notifyModelList = notifyModelList;
    }

    public UserModel(String username, String password, String name, String address, int imgUser, List<BookModel> booksUser, List<BookModel> booksUserRecommend) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.imgUser = imgUser;
        this.booksUser = booksUser;
        this.booksUserRecommend = booksUserRecommend;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        final UserModel other = (UserModel) obj;
        if (other.getUsername() != null && Objects.equals(other.getUsername(), this.username)) {
            return true;
        }
        return super.equals(obj);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImgUser() {
        return imgUser;
    }

    public void setImgUser(int imgUser) {
        this.imgUser = imgUser;
    }

    public List<BookModel> getBooksUser() {
        return booksUser;
    }

    public void setBooksUser(List<BookModel> booksUser) {
        this.booksUser = booksUser;
    }

    public List<BookModel> getBooksUserReview() {
        return booksUserReview;
    }

    public void setBooksUserReview(List<BookModel> booksUserReview) {
        this.booksUserReview = booksUserReview;
    }

    public List<BookRequestModel> getBooksUserRequest() {
        return booksUserRequest;
    }

    public void setBooksUserRequest(List<BookRequestModel> booksUserRequest) {
        this.booksUserRequest = booksUserRequest;
    }

    public List<BookModel> getBooksUserRecommend() {
        return booksUserRecommend;
    }

    public void setBooksUserRecommend(List<BookModel> booksUserRecommend) {
        this.booksUserRecommend = booksUserRecommend;
    }

    public List<NotifyModel> getNotifyModelList() {
        return notifyModelList;
    }

    public void setNotifyModelList(List<NotifyModel> notifyModelList) {
        this.notifyModelList = notifyModelList;
    }
}
