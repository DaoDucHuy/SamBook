package com.sam.sambook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.BookReviewedModel;
import com.sam.sambook.model.UserModel;

public class ReviewContentActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName;
    private TextView userAddress;
    private RatingBar ratingBar;
    private TextView reviewContent;

    private BookReviewedModel bookReviewedModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_content);
        setTitle("");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
        getReview();

    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init() {
        userImage = (ImageView) findViewById(R.id.review_content_user_image);
        userName = (TextView) findViewById(R.id.review_content_user_name);
        userAddress = (TextView) findViewById(R.id.review_content_user_address);
        ratingBar = (RatingBar) findViewById(R.id.review_content_rating_bar);
        reviewContent = (TextView) findViewById(R.id.review_content_content);

        getReview();
        if (bookReviewedModel != null) {
            userImage.setImageResource(bookReviewedModel.getUserReviewBook().getImgUser());
            userName.setText(bookReviewedModel.getUserReviewBook().getName());
            userAddress.setText(bookReviewedModel.getUserReviewBook().getAddress());
            ratingBar.setRating(bookReviewedModel.getNumbRating());
            reviewContent.setText(bookReviewedModel.getReviewContent());
        }
        else {

        }
    }

    private void getReview() {
        bookReviewedModel = (BookReviewedModel) getIntent().getSerializableExtra("REVIEW");
        if (bookReviewedModel != null) {
//            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_SHORT).show();
        }
    }
}