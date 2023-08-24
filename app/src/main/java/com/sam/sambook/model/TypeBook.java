package com.sam.sambook.model;

import java.io.Serializable;
import java.util.List;

public class TypeBook implements Serializable {
    private String nameType;
    private List<BookModel> bookModels;

    public TypeBook() {
    }

    public TypeBook(String nameType, List<BookModel> bookModels) {
        this.nameType = nameType;
        this.bookModels = bookModels;
    }

    public TypeBook(String nameType) {
        this.nameType = nameType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public List<BookModel> getBookModels() {
        return bookModels;
    }

    public void setBookModels(List<BookModel> bookModels) {
        this.bookModels = bookModels;
    }
}
