package com.sam.sambook.model;

import java.io.Serializable;

public class BookReviewedModel implements Serializable {

    private String titleBook;
    private UserModel userReviewBook;
    private String reviewContent;
    private float numbRating;

    public BookReviewedModel(){

    }

    public BookReviewedModel(String titleBook) {
        this.titleBook = titleBook;
    }

    public BookReviewedModel(String titleBook, UserModel userReviewBook) {
        this.titleBook = titleBook;
        this.userReviewBook = userReviewBook;
    }

    public BookReviewedModel(String titleBook, UserModel userReviewBook, float numbRating) {
        this.titleBook = titleBook;
        this.userReviewBook = userReviewBook;
        this.numbRating = numbRating;
    }

    public BookReviewedModel(String titleBook, UserModel userReviewBook, String reviewContent, float numbRating) {
        this.titleBook = titleBook;
        this.userReviewBook = userReviewBook;
        this.reviewContent = reviewContent;
        this.numbRating = numbRating;
    }

    public String getTitleBook() {
        return titleBook;
    }

    public void setTitleBook(String titleBook) {
        this.titleBook = titleBook;
    }

    public UserModel getUserReviewBook() {
        return userReviewBook;
    }

    public void setUserReviewBook(UserModel userReviewBook) {
        this.userReviewBook = userReviewBook;
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
