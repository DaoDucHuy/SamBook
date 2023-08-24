package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.fragmentBookDetail.BookDetailPagerAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;

public class BookDetailActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button bt_addToShelf;
    private Button bt_borrow;

    private BookModel _bookModel;
    private UserModel _userModel;
    private ArrayList<BookModel> listBookUser;
    private ArrayList<UserModel> listUserInBook;

    private TabLayout tabLayout;

    private GifImageView img;
    private TextView name;
    private TextView author;
    private RatingBar ratingBar;
    private TextView rating;
    private TextView numbReader;
    private TextView numbBorrower;

//    private String url = "http://" + "10.0.2.2" + ":" + 5000 + "/";
    private String url = "http://" + "172.16.66.2" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        setTitle("");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img = findViewById(R.id.book_detail_book_image);
        name = findViewById(R.id.book_detail_title);
        author = findViewById(R.id.book_detail_author);
        ratingBar = findViewById(R.id.book_detail_rating_bar);
        rating = findViewById(R.id.book_detail_rating);
        numbReader = findViewById(R.id.book_detail_numb_reader);
        numbBorrower = findViewById(R.id.book_detail_numb_borrower);

        // Initialize activity
        init();

        // Send data to the child Fragments

        // Button onClickListener
        // Need to write the onClickListener for adding one book to user's shelf
        bt_addToShelf = (Button) findViewById(R.id.bt_book_detail_add_to_shelf);
        bt_addToShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddBook();
            }
        });

        //Need to write the onClickListener for user to borrow one book
        bt_borrow = (Button) findViewById(R.id.bt_book_detail_borrow);
        bt_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BorrowActivity.class);
                intent.putExtra("BOOKDETAIL", _bookModel);
                intent.putExtra("USER", _userModel);
                startActivity(intent);
            }
        });


        //TabLayout w/ ViewPager configuring
        tabLayout = (TabLayout) findViewById(R.id.book_detail_tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.book_detail_pager);
        final BookDetailPagerAdapter adapter = new BookDetailPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        String activity = (String) getIntent().getSerializableExtra("FROM_ACTIVITY");
        if (activity.equals("Main")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("USER", _userModel);
            startActivity(intent);
        }
        else if (activity.equals("ListMoreBook")) {
            Intent intent = new Intent(this, ListMoreBookActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("USER", _userModel);
            startActivity(intent);
        }
    }

    private void _updateBookUser() {
        if (_userModel.getBooksUser() != null) {
            listBookUser = (ArrayList<BookModel>) _userModel.getBooksUser();
        } else {
            listBookUser = new ArrayList<>();
        }

        int numbReader = _bookModel.getNumbReader();
        numbReader++;
        _bookModel.setNumbReader(numbReader);

//        BookModel bookModel = new BookModel(_bookModel.getName(), _bookModel.getAuthor(), _bookModel.getImage(), _bookModel.getNumbReader(), _bookModel.getNumbBorrow(),
//                _bookModel.getDescription(), _bookModel.getNumbRating(), _bookModel.getTypeBook());
        BookModel bookModel = new BookModel(_bookModel.getName(), _bookModel.getAuthor(), _bookModel.getDescription(), _bookModel.getTypeBook(), true);
        listBookUser.add(bookModel);
        _userModel.setBooksUser(listBookUser);

        _pushListBookUser(_userModel); //push to data user

    }

    private void _pushListBookUser(UserModel userModel) {

        //push to data user
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(userModel.getUsername()).setValue(userModel);

//        try {
//            Gson gson = new Gson();
//            String user_gson = gson.toJson(_userModel);
//            OutputStream saveFile = openFileOutput("USERSAMBOOK.txt", MODE_PRIVATE);
//            saveFile.write(user_gson.getBytes());
//            saveFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        postRequest(_userModel.getUsername(), url);
        new Post().execute();

        Toast.makeText(getApplicationContext(), "Thêm sách vào kệ sách thành công", Toast.LENGTH_SHORT).show();
    }

    private void _updateUserToBook() {
        if (_bookModel.getUserModelList() != null){
            listUserInBook = (ArrayList<UserModel>) _bookModel.getUserModelList();
        } else {
            listUserInBook = new ArrayList<>();
        }

        UserModel userModel = new UserModel(_userModel.getUsername(), _userModel.getName(), _userModel.getAddress(), _userModel.getImgUser());
        listUserInBook.add(userModel);
        _bookModel.setUserModelList(listUserInBook);

        _pushListUserToBookModel(_bookModel); //push user to userModelList in book data
    }

    private void _pushListUserToBookModel(BookModel bookModel) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        myRef.child(bookModel.getName()).setValue(bookModel);

//        new DownloadImageTask(img)
//                .execute(bookModel.getImage());
        Picasso.get().load(bookModel.getImage()).into(img);
        name.setText(bookModel.getName());
        author.setText(bookModel.getAuthor());
        ratingBar.setRating(bookModel.getNumbRating());
        rating.setText(String.valueOf(bookModel.getNumbRating()));
        numbReader.setText(String.valueOf(bookModel.getNumbReader()));
        numbBorrower.setText(String.valueOf(bookModel.getNumbBorrow()));
    }

    private void init() {
        //Receive data from the MainActivity and Set data for the view
        getBookDetail();
        getUser();

        if (_bookModel != null) {
//            img.setImageResource(_bookModel.getImage());
//            new DownloadImageTask(img)
//                    .execute(_bookModel.getImage());
            Picasso.get().load(_bookModel.getImage()).into(img);
            name.setText(_bookModel.getName());
            author.setText(_bookModel.getAuthor());
            ratingBar.setRating(_bookModel.getNumbRating());
            rating.setText(String.valueOf(_bookModel.getNumbRating()));
            numbReader.setText(String.valueOf(_bookModel.getNumbReader()));
            numbBorrower.setText(String.valueOf(_bookModel.getNumbBorrow()));
        }
    }

    private void getBookDetail() {
        _bookModel = (BookModel) getIntent().getSerializableExtra("BOOKDETAIL");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        myRef.child(_bookModel.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _bookModel = new BookModel();
                    _bookModel = snapshot.getValue(BookModel.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("BookDetail", _bookModel.getName());
    }

    private void getUser() {
        _userModel = (UserModel) getIntent().getSerializableExtra("USER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _userModel = new UserModel();
                    _userModel = snapshot.getValue(UserModel.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("User", _userModel.getUsername());
    }

    private void showAlertDialogAddBook() {
        Log.e("PUSH BOOK", "PUSHING");
        AlertDialog.Builder builder = new AlertDialog.Builder(BookDetailActivity.this);
        builder.setTitle("Bạn có muốn thêm cuốn sách này vào kệ sách của bạn?");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _updateBookUser();
                _updateUserToBook();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    //http post request to Python server
    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();

        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Async task for postRequest
    private class Post extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                postRequest(_userModel.getUsername(), url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //Download image task for loading book's image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        GifImageView imageView;

        public DownloadImageTask(GifImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}