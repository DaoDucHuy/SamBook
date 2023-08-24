package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toolbar;

import com.sam.sambook.R;

public class EditReviewActivity extends AppCompatActivity {

    private EditText reviewContent;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        setTitle("");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reviewContent = findViewById(R.id.edit_review_edit_text);
        ratingBar = findViewById(R.id.edit_review_rating_bar_active);
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Back confirmation
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.edit_review_exit_confirm_title))
                .setMessage(getString(R.string.edit_review_exit_confirm))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.no), null)
                .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_CANCELED);
                        EditReviewActivity.super.onBackPressed();
                    }
                })
                .show();
    }

    //Setup menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_review_action_bar, menu);
        return true;
    }

    //Setup menu items event handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_confirm_review_content) {
            String reviewContent = this.reviewContent.getText().toString();
            float ratingScore = (float) this.ratingBar.getRating();
            Intent intent = new Intent();
            intent.putExtra("REVIEWCONTENT", reviewContent);
            intent.putExtra("RATINGSCORE", ratingScore);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}