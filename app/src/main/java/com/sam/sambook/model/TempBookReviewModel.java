package com.sam.sambook.model;

import java.io.Serializable;

public class TempBookReviewModel implements Serializable {

    private UserModel userModel;
    private String reviewContent;
    private float numbRating;

    public TempBookReviewModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public TempBookReviewModel(UserModel userModel, String reviewContent) {
        this.userModel = userModel;
        this.reviewContent = reviewContent;
    }

    public TempBookReviewModel(UserModel userModel, String reviewContent, float numbRating) {
        this.userModel = userModel;
        this.reviewContent = reviewContent;
        this.numbRating = numbRating;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public float getNumbRating() {
        return numbRating;
    }

    public void setNumbRating(float numbRating) {
        this.numbRating = numbRating;
    }
}
