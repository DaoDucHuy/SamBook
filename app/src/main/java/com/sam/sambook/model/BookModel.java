package com.sam.sambook.model;

import java.io.Serializable;
import java.util.List;

public class BookModel implements Serializable {

    private String name;
    private String author;
    private String image;
    private int numbReader;
    private int numbBorrow;
    private String description;
    private float numbRating;
    private String typeBook;
    private Boolean status;
    private List<BookReviewedModel> bookReviewedModel;
    private List<UserModel> userModelList;


    public BookModel() {

    }

    public BookModel(String name) {
        this.name = name;
    }

    public BookModel(String name, Boolean status) {
        this.name = name;
        this.status = status;
    }

    public BookModel(String name, String author, String image) {
        this.name = name;
        this.author = author;
        this.image = image;
    }

    public BookModel(String name, List<UserModel> userModelList) {
        this.name = name;
        this.userModelList = userModelList;
    }

    public BookModel(String author, String description, String image, String name, int numbBorrow, float numbRating, int numbReader, String typeBook) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.numbReader = numbReader;
        this.numbBorrow = numbBorrow;
        this.description = description;
        this.numbRating = numbRating;
        this.typeBook = typeBook;
    }

    public BookModel(String name, String author, String image, int numbReader, int numbBorrow, String description, float numbRating, String typeBook) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.numbReader = numbReader;
        this.numbBorrow = numbBorrow;
        this.description = description;
        this.numbRating = numbRating;
        this.typeBook = typeBook;
    }

    public BookModel(String name, String author, String image, int numbReader, int numbBorrow, String description, float numbRating, String typeBook, List<BookReviewedModel> bookReviewedModel, List<UserModel> userModelList) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.numbReader = numbReader;
        this.numbBorrow = numbBorrow;
        this.description = description;
        this.numbRating = numbRating;
        this.typeBook = typeBook;
        this.bookReviewedModel = bookReviewedModel;
        this.userModelList = userModelList;
    }

    public BookModel(String name, String author, String image, int numbReader, int numbBorrow, String description, float numbRating, String typeBook, Boolean status, List<BookReviewedModel> bookReviewedModel, List<UserModel> userModelList) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.numbReader = numbReader;
        this.numbBorrow = numbBorrow;
        this.description = description;
        this.numbRating = numbRating;
        this.typeBook = typeBook;
        this.status = status;
        this.bookReviewedModel = bookReviewedModel;
        this.userModelList = userModelList;
    }

    public BookModel(String name, String author, String description, String typeBook, Boolean status) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.typeBook = typeBook;
        this.status = status;
    }

    public BookModel(String name, String author, String description, String typeBook) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.typeBook = typeBook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumbReader() {
        return numbReader;
    }

    public void setNumbReader(int numbReader) {
        this.numbReader = numbReader;
    }

    public int getNumbBorrow() {
        return numbBorrow;
    }

    public void setNumbBorrow(int numbBorrow) {
        this.numbBorrow = numbBorrow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getNumbRating() {
        return numbRating;
    }

    public void setNumbRating(float numbRating) {
        this.numbRating = numbRating;
    }

    public String getTypeBook() {
        return typeBook;
    }

    public void setTypeBook(String typeBook) {
        this.typeBook = typeBook;
    }

    public List<BookReviewedModel> getBookReviewedModel() {
        return bookReviewedModel;
    }

    public void setBookReviewedModel(List<BookReviewedModel> bookReviewedModel) {
        this.bookReviewedModel = bookReviewedModel;
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    public void setUserModelList(List<UserModel> userModelList) {
        this.userModelList = userModelList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void ratingCal() {
        if (this.bookReviewedModel != null) {
            float rating = 0;
            for (int i = 0; i < bookReviewedModel.size(); i++) {
                rating += bookReviewedModel.get(i).getNumbRating();
            }
            rating = rating / bookReviewedModel.size();
            this.setNumbRating(rating);
        }
    }
}
