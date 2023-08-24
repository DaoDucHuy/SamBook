package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.BookRequestModel;
import com.sam.sambook.model.NotifyModel;
import com.sam.sambook.model.UserModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class BorrowDetailActivity extends AppCompatActivity {

    private TextView receiverUserName;
    private GifImageView bookImage;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookNumbReader;
    private TextView bookNumbBorrower;
    private TextView bookNumbRating;
    private TextView requestSendDate;
    private ImageView receiverImage;
    private TextView receiverName;
    private TextView receiverAddress;
    private ImageButton message;
    private TextView receiverUserNameMessage;
    private Button sendRequest;

    private UserModel _sender;
    private UserModel _receiver;
    private BookModel _bookModel;
    private NotifyModel _notifyModel;
    private BookRequestModel _bookRequestModel;

    private ArrayList<NotifyModel> notifyModelArrayList;
    private ArrayList<BookRequestModel> receiverRequestArrayList;
    private ArrayList<BookRequestModel> senderRequestArrayList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_detail);
        setTitle("");
        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSender();

        init();

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("OTHERUSER", _receiver);
                intent.putExtra("USER", _sender);
                startActivity(intent);
            }
        });

        receiverUserNameMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("OTHERUSER", _receiver);
                intent.putExtra("USER", _sender);
                startActivity(intent);
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("OTHERUSER", _receiver);
        intent.putExtra("USER", _sender);
        startActivity(intent);
    }

    private void init() {
        receiverUserName = findViewById(R.id.borrow_detail_user_username);
        bookImage = findViewById(R.id.borrow_detail_book_image);
        bookTitle = findViewById(R.id.borrow_detail_book_title);
        bookAuthor = findViewById(R.id.borrow_detail_book_author);
        bookNumbReader = findViewById(R.id.borrow_detail_numb_reader);
        bookNumbBorrower = findViewById(R.id.borrow_detail_numb_borrower);
        bookNumbRating = findViewById(R.id.borrow_detail_numb_rating);
        requestSendDate = findViewById(R.id.borrow_detail_request_send_date);
        receiverImage = findViewById(R.id.review_content_user_image);
        receiverName = findViewById(R.id.borrow_detail_user_name);
        receiverAddress = findViewById(R.id.borrow_detail_user_address);
        message = findViewById(R.id.borrow_detail_message);
        receiverUserNameMessage = findViewById(R.id.borrow_detail_user_name_message);
        sendRequest = findViewById(R.id.borrow_detail_send_request);
    }

    private void reload() {
        receiverUserName.setText(_receiver.getUsername());
//        new DownloadImageTask(bookImage)
//                .execute(_bookModel.getImage());
        Picasso.get().load(_bookModel.getImage()).into(bookImage);
        bookTitle.setText(_bookModel.getName());
        bookAuthor.setText(_bookModel.getAuthor());
        bookNumbReader.setText(String.valueOf(_bookModel.getNumbReader()));
        bookNumbBorrower.setText(String.valueOf(_bookModel.getNumbBorrow()));
        bookNumbRating.setText(String.valueOf(_bookModel.getNumbRating()));
        requestSendDate.setText(_notifyModel.getDate());
//        receiverImage.setImageBitmap();
        receiverName.setText(_receiver.getName());
        receiverAddress.setText(_receiver.getAddress());
        receiverUserNameMessage.setText(_receiver.getUsername());
    }

    private void getSender() {
        _sender = (UserModel) getIntent().getSerializableExtra("SENDER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_sender.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _sender = snapshot.getValue(UserModel.class);
                    if (_sender.getBooksUserRequest() != null) {
                        senderRequestArrayList = (ArrayList<BookRequestModel>) _sender.getBooksUserRequest();
                    } else {
                        senderRequestArrayList = new ArrayList<>();
                    }
                    getReceiver();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getReceiver() {
        _receiver = (UserModel) getIntent().getSerializableExtra("RECEIVER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_receiver.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _receiver = snapshot.getValue(UserModel.class);
                    if (_receiver.getBooksUserRequest() != null) {
                        receiverRequestArrayList = (ArrayList<BookRequestModel>) _receiver.getBooksUserRequest();
                    } else {
                        receiverRequestArrayList = new ArrayList<>();
                    }
                    if (_receiver.getNotifyModelList() != null) {
                        notifyModelArrayList = (ArrayList<NotifyModel>) _receiver.getNotifyModelList();
                    } else {
                        notifyModelArrayList = new ArrayList<>();
                    }
                    getBookDetail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBookDetail() {
        _bookModel = (BookModel) getIntent().getSerializableExtra("BOOKDETAIL");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        myRef.child(_bookModel.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _bookModel = snapshot.getValue(BookModel.class);
                    getNotify();
                    getRequest();
                    reload();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNotify() {
        _notifyModel = (NotifyModel) getIntent().getSerializableExtra("NOTIFY");
        notifyModelArrayList.add(_notifyModel);
    }

    private void getRequest() {
        UserModel sender = new UserModel(_sender.getUsername(), _sender.getName(), _sender.getAddress(), _sender.getImgUser());
        UserModel receiver = new UserModel(_receiver.getUsername(), _receiver.getName(), _receiver.getAddress(), _receiver.getImgUser());
        BookModel bookModel = new BookModel(_bookModel.getName(), _bookModel.getAuthor(), _bookModel.getDescription(), _bookModel.getTypeBook());
        _bookRequestModel = new BookRequestModel(sender, receiver, bookModel);
        senderRequestArrayList.add(_bookRequestModel);
        receiverRequestArrayList.add(_bookRequestModel);
    }

    private void sendRequest() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        _sender.setBooksUserRequest(senderRequestArrayList);
        _receiver.setBooksUserRequest(receiverRequestArrayList);
        _receiver.setNotifyModelList(notifyModelArrayList);

        myRef.child(_sender.getUsername()).setValue(_sender);
        myRef.child(_receiver.getUsername()).setValue(_receiver);

        Toast.makeText(this.getApplicationContext(), "Yêu cầu của bạn sẽ được gửi tới người dùng " + _receiver.getUsername(), Toast.LENGTH_SHORT).show();

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _sender);
                startActivity(intent);
            }
        }.start();
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