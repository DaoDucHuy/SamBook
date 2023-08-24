package com.sam.sambook.manager;

import com.sam.sambook.model.BookModel;

import java.util.ArrayList;

public class BookManager {

    public static ArrayList<BookModel> createbook(int[] imgBooks, String[] nameBooks, String[] authorBooks) {
        ArrayList<BookModel> list = new ArrayList<>();

        for (int i = 0; i < nameBooks.length; i++) {
            BookModel bookModel = new BookModel();
            bookModel.setImage(String.valueOf(imgBooks[i]));
            bookModel.setName(nameBooks[i]);
            bookModel.setAuthor(authorBooks[i]);
            list.add(bookModel);
        }

        return list;
    }

    public static ArrayList<BookModel> createReviewBook(int[] img, String[] nameBook, String[] author, float[] ratings) {
        ArrayList<BookModel> list = new ArrayList<>();

        for (int i = 0; i < nameBook.length; i++) {
            BookModel bookModel = new BookModel();
            bookModel.setImage(String.valueOf(img[i]));
            bookModel.setName(nameBook[i]);
            bookModel.setAuthor(author[i]);
            bookModel.setNumbRating(ratings[i]);
            list.add(bookModel);
        }

        return list;
    }

//    public static ArrayList<BookReviewedModel> createReviewedBook(int[] img, ) {
//
//    }

}
