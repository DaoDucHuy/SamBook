package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class RequestDetailActivity extends AppCompatActivity {

    //Layout
    private LinearLayout receiverPending;
    private LinearLayout receiverAccepted;
    private LinearLayout sender;

    //View
    private ImageView userImage;
    private TextView userUserName;
    private TextView userName;
    private TextView userAddress;

    private ImageButton message;
    private TextView messageUserName;

    private TextView requestTitle;
    private GifImageView bookImage;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView numbReader;
    private TextView numbBorrower;
    private TextView numbRating;

    private Button receiverPendingAccept;
    private Button receiverPendingCancel;

    private LinearLayout receiverAcceptedContacting;
    private ImageView receiverAcceptedLendCheck;
    private Button receiverAcceptedLend;
    private ImageView receiverAcceptedLendCheckChecked;
    private Button receiverAcceptedLendChecked;
    private ImageView receiverAcceptedReturnCheck;
    private Button receiverAcceptedReturn;
    private ImageView receiverAcceptedReturnCheckChecked;
    private Button receiverAcceptedReturnChecked;
    private Button receiverAcceptedCancel;
    private Button receiverAcceptedLost;

    private Button senderContact;
    private Button senderLend;
    private Button senderReturn;
    private Button senderCancel;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserModel _userModel;
    private UserModel _userModelOther;
    private BookRequestModel _bookRequestModel;
    private BookModel _bookModel;
    private ArrayList<NotifyModel> _notifyModelArrayList;
    private ArrayList<NotifyModel> _notifyModelOtherArrayList;
    private ArrayList<BookRequestModel> _bookRequestModelArrayList;
    private ArrayList<BookRequestModel> _bookRequestModelOtherArrayList;

    private Boolean isSender = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        setTitle("");
        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getUser();

        init();

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("OTHERUSER", _userModelOther);
                intent.putExtra("USER", _userModel);
                startActivity(intent);
            }
        });

        messageUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        receiverPendingAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("contacting");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã đồng ý yêu cầu của bạn", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                reload();
                updateUser();
                updateOtherUser();
            }
        });

        receiverPendingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("canceled");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã từ chối yêu cầu của bạn", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                reload();
                updateUser();
                updateOtherUser();
            }
        });

        receiverAcceptedLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("lending");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "contacting") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "contacting") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã cho bạn mượn sách", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                for (int i = 0; i < _userModel.getBooksUser().size(); i++) {
                    if (Objects.equals(_userModel.getBooksUser().get(i).getName(), _bookModel.getName()) && _userModel.getBooksUser().get(i).getStatus()) {
                        _userModel.getBooksUser().get(i).setStatus(false);
                        break;
                    }
                }

                int numbBorrow = _bookModel.getNumbBorrow();
                numbBorrow++;
                _bookModel.setNumbBorrow(numbBorrow);

                reload();
                updateUser();
                updateOtherUser();
                updateBook();
            }
        });

        receiverAcceptedReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("returning");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "lending") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "lending") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã nhận lại sách từ bạn", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                for (int i = 0; i < _userModel.getBooksUser().size(); i++) {
                    if (Objects.equals(_userModel.getBooksUser().get(i).getName(), _bookModel.getName()) && !_userModel.getBooksUser().get(i).getStatus()) {
                        _userModel.getBooksUser().get(i).setStatus(true);
                        break;
                    }
                }

                reload();
                updateUser();
                updateOtherUser();
            }
        });

        receiverAcceptedCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("canceled");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "contacting") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "contacting") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã từ chối yêu cầu của bạn", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                reload();
                updateUser();
                updateOtherUser();
            }
        });

        receiverAcceptedLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("lost");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "lending") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "lending") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã báo cáo sách của họ bị thất lạc", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                reload();
                updateUser();
                updateOtherUser();
            }
        });

        senderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bookRequestModel.setStatus("canceled");

                for (int i = 0; i < _bookRequestModelArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelArrayList.set(i, _bookRequestModel);
                    }
                }

                for (int i = 0; i < _bookRequestModelOtherArrayList.size(); i++) {
                    if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserSendRequest().getUsername())
                            && Objects.equals(_bookRequestModel.getUserReceiveRequest().getUsername(), _bookRequestModelOtherArrayList.get(i).getUserReceiveRequest().getUsername())
                            && Objects.equals(_bookRequestModelOtherArrayList.get(i).getStatus(), "pending") && Objects.equals(_bookRequestModelOtherArrayList.get(i).getBookModel().getName(), _bookModel.getName())) {
                        _bookRequestModelOtherArrayList.set(i, _bookRequestModel);
                    }
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                NotifyModel notifyModel = new NotifyModel(_userModel.getUsername() + " đã hủy yêu cầu mượn sách", _bookModel.getName(), formattedDate);
                _notifyModelOtherArrayList.add(notifyModel);

                reload();
                updateUser();
                updateOtherUser();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("USER", _userModel);
        startActivity(intent);
    }

    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("OTHERUSER", _userModelOther);
        intent.putExtra("USER", _userModel);
        startActivity(intent);
    }

    private void init() {
        userImage = findViewById(R.id.request_detail_user_image);
        userUserName = findViewById(R.id.request_detail_user_username);
        userName = findViewById(R.id.request_detail_user_name);
        userAddress = findViewById(R.id.request_detail_user_address);

        message = findViewById(R.id.request_detail_message);
        messageUserName = findViewById(R.id.request_detail_user_name_message);

        requestTitle = findViewById(R.id.request_detail_title);
        bookImage = findViewById(R.id.request_detail_book_image);
        bookTitle = findViewById(R.id.request_detail_book_title);
        bookAuthor = findViewById(R.id.request_detail_book_author);
        numbReader = findViewById(R.id.request_detail_numb_reader);
        numbBorrower = findViewById(R.id.request_detail_numb_borrower);
        numbRating = findViewById(R.id.request_detail_numb_rating);

        receiverPending = findViewById(R.id.request_detail_receiver_pending);
        receiverPendingAccept = findViewById(R.id.request_detail_receiver_pending_accept);
        receiverPendingCancel = findViewById(R.id.request_detail_receiver_pending_cancel);

        receiverAccepted = findViewById(R.id.request_detail_receiver_accepted);
        receiverAcceptedContacting = findViewById(R.id.request_detail_receiver_contacting);
        receiverAcceptedLendCheck = findViewById(R.id.request_detail_receiver_accepted_lend_check);
        receiverAcceptedLend = findViewById(R.id.request_detail_receiver_accepted_lend);
        receiverAcceptedLendCheckChecked = findViewById(R.id.request_detail_receiver_accepted_lend_check_checked);
        receiverAcceptedLendChecked = findViewById(R.id.request_detail_receiver_accepted_lend_checked);
        receiverAcceptedReturnCheck = findViewById(R.id.request_detail_receiver_accepted_return_check);
        receiverAcceptedReturn = findViewById(R.id.request_detail_receiver_accepted_return);
        receiverAcceptedReturnCheckChecked = findViewById(R.id.request_detail_receiver_accepted_return_check_checked);
        receiverAcceptedReturnChecked = findViewById(R.id.request_detail_receiver_accepted_return_checked);
        receiverAcceptedCancel = findViewById(R.id.request_detail_receiver_accepted_cancel);
        receiverAcceptedLost = findViewById(R.id.request_detail_receiver_accepted_lost);

        sender = findViewById(R.id.request_detail_sender);
        senderContact = findViewById(R.id.request_detail_sender_contact);
        senderLend = findViewById(R.id.request_detail_sender_lend);
        senderReturn = findViewById(R.id.request_detail_sender_return);
        senderCancel = findViewById(R.id.request_detail_sender_cancel);
    }

    private void reload() {
        if (isSender) {
            receiverPending.setVisibility(View.GONE);
            receiverAccepted.setVisibility(View.GONE);
            sender.setVisibility(View.VISIBLE);

            switch (_bookRequestModel.getStatus()) {
                case "pending":
                    senderContact.setVisibility(View.GONE);
                    senderLend.setVisibility(View.GONE);
                    senderReturn.setVisibility(View.GONE);
                    senderCancel.setVisibility(View.VISIBLE);
                    senderCancel.setEnabled(true);
                    break;
                case "contacting":
                    senderContact.setVisibility(View.VISIBLE);
                    senderLend.setVisibility(View.GONE);
                    senderReturn.setVisibility(View.GONE);
                    senderCancel.setVisibility(View.VISIBLE);
                    break;
                case "lending":
                    senderContact.setVisibility(View.VISIBLE);
                    senderLend.setVisibility(View.VISIBLE);
                    senderReturn.setVisibility(View.GONE);
                    senderCancel.setVisibility(View.GONE);
                    break;
                case "returning":
                    senderContact.setVisibility(View.VISIBLE);
                    senderLend.setVisibility(View.VISIBLE);
                    senderReturn.setVisibility(View.VISIBLE);
                    senderCancel.setVisibility(View.GONE);
                    break;
                case "canceled":
                    senderContact.setVisibility(View.GONE);
                    senderLend.setVisibility(View.GONE);
                    senderReturn.setVisibility(View.GONE);
                    senderCancel.setVisibility(View.VISIBLE);
                    senderCancel.setEnabled(false);
                    break;
            }

            requestTitle.setText("Bạn đã gửi yêu cầu mượn sách");

        } else {
            if (Objects.equals(_bookRequestModel.getStatus(), "pending")) {
                receiverPending.setVisibility(View.VISIBLE);
                receiverAccepted.setVisibility(View.GONE);
                sender.setVisibility(View.GONE);

            } else {
                receiverPending.setVisibility(View.GONE);
                receiverAccepted.setVisibility(View.VISIBLE);
                sender.setVisibility(View.GONE);

                switch (_bookRequestModel.getStatus()) {
                    case "contacting":
                        receiverAcceptedContacting.setVisibility(View.VISIBLE);
                        receiverAcceptedLendCheck.setVisibility(View.VISIBLE);
                        receiverAcceptedLend.setVisibility(View.VISIBLE);
                        receiverAcceptedLendCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedLendChecked.setVisibility(View.GONE);
                        receiverAcceptedReturn.setVisibility(View.VISIBLE);
                        receiverAcceptedReturnCheck.setVisibility(View.VISIBLE);
                        receiverAcceptedReturn.setEnabled(false);
                        receiverAcceptedReturnCheck.setEnabled(false);
                        receiverAcceptedReturnChecked.setVisibility(View.GONE);
                        receiverAcceptedReturnCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedCancel.setVisibility(View.VISIBLE);
                        receiverAcceptedLost.setVisibility(View.GONE);
                        break;
                    case "lending":
                        receiverAcceptedContacting.setVisibility(View.VISIBLE);
                        receiverAcceptedLendCheck.setVisibility(View.GONE);
                        receiverAcceptedLend.setVisibility(View.GONE);
                        receiverAcceptedLendCheckChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedLendChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedReturn.setVisibility(View.VISIBLE);
                        receiverAcceptedReturnCheck.setVisibility(View.VISIBLE);
                        receiverAcceptedReturn.setEnabled(true);
                        receiverAcceptedReturnCheck.setEnabled(true);
                        receiverAcceptedReturnChecked.setVisibility(View.GONE);
                        receiverAcceptedReturnCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedCancel.setVisibility(View.GONE);
                        receiverAcceptedLost.setVisibility(View.VISIBLE);
                        break;
                    case "returning":
                        receiverAcceptedContacting.setVisibility(View.VISIBLE);
                        receiverAcceptedLendCheck.setVisibility(View.GONE);
                        receiverAcceptedLend.setVisibility(View.GONE);
                        receiverAcceptedLendCheckChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedLendChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedReturn.setVisibility(View.GONE);
                        receiverAcceptedReturnCheck.setVisibility(View.GONE);
                        receiverAcceptedReturnChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedReturnCheckChecked.setVisibility(View.VISIBLE);
                        receiverAcceptedCancel.setVisibility(View.GONE);
                        receiverAcceptedLost.setVisibility(View.GONE);
                        break;
                    case "canceled":
                        receiverAcceptedContacting.setVisibility(View.GONE);
                        receiverAcceptedLendCheck.setVisibility(View.GONE);
                        receiverAcceptedLend.setVisibility(View.GONE);
                        receiverAcceptedLendCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedLendChecked.setVisibility(View.GONE);
                        receiverAcceptedReturn.setVisibility(View.GONE);
                        receiverAcceptedReturnCheck.setVisibility(View.GONE);
                        receiverAcceptedReturnChecked.setVisibility(View.GONE);
                        receiverAcceptedReturnCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedCancel.setVisibility(View.VISIBLE);
                        receiverAcceptedCancel.setEnabled(false);
                        receiverAcceptedLost.setVisibility(View.GONE);
                        break;
                    case "lost":
                        receiverAcceptedContacting.setVisibility(View.GONE);
                        receiverAcceptedLendCheck.setVisibility(View.GONE);
                        receiverAcceptedLend.setVisibility(View.GONE);
                        receiverAcceptedLendCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedLendChecked.setVisibility(View.GONE);
                        receiverAcceptedReturn.setVisibility(View.GONE);
                        receiverAcceptedReturnCheck.setVisibility(View.GONE);
                        receiverAcceptedReturnChecked.setVisibility(View.GONE);
                        receiverAcceptedReturnCheckChecked.setVisibility(View.GONE);
                        receiverAcceptedCancel.setVisibility(View.GONE);
                        receiverAcceptedLost.setVisibility(View.VISIBLE);
                        receiverAcceptedLost.setEnabled(false);
                        break;
                }
            }

            requestTitle.setText("Đã gửi yêu cầu mượn sách");
        }

        userUserName.setText(_userModelOther.getUsername());
        userName.setText(_userModelOther.getName());
        userAddress.setText(_userModelOther.getAddress());

        messageUserName.setText(_userModelOther.getUsername());

//        new DownloadImageTask(bookImage).execute(_bookModel.getImage());
        Picasso.get().load(_bookModel.getImage()).into(bookImage);
        bookTitle.setText(_bookModel.getName());
        bookAuthor.setText(_bookModel.getAuthor());
        numbReader.setText(String.valueOf(_bookModel.getNumbReader()));
        numbBorrower.setText(String.valueOf(_bookModel.getNumbBorrow()));
        numbRating.setText(String.valueOf(_bookModel.getNumbRating()));
    }

    private void getUser() {
        _userModel = (UserModel) getIntent().getSerializableExtra("USER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _userModel = snapshot.getValue(UserModel.class);
                    if (_userModel.getNotifyModelList() != null) {
                        _notifyModelArrayList = (ArrayList<NotifyModel>) _userModel.getNotifyModelList();
                    } else {
                        _notifyModelArrayList = new ArrayList<>();
                    }
                    if (_userModel.getBooksUserRequest() != null) {
                        _bookRequestModelArrayList = (ArrayList<BookRequestModel>) _userModel.getBooksUserRequest();
                    } else {
                        _bookRequestModelArrayList = new ArrayList<>();
                    }
                    getBookRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBookRequest() {
        _bookRequestModel = (BookRequestModel) getIntent().getSerializableExtra("REQUEST");

        if (Objects.equals(_bookRequestModel.getUserSendRequest().getUsername(), _userModel.getUsername())) {
            getOtherUser(isSender);
        } else {
            isSender = false;
            getOtherUser(isSender);
        }
    }

    private void getOtherUser(Boolean isSender) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        if (isSender) {
            myRef.child(_bookRequestModel.getUserReceiveRequest().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        _userModelOther = snapshot.getValue(UserModel.class);
                        if (_userModelOther.getNotifyModelList() != null) {
                            _notifyModelOtherArrayList = (ArrayList<NotifyModel>) _userModelOther.getNotifyModelList();
                        } else {
                            _notifyModelOtherArrayList = new ArrayList<>();
                        }
                        if (_userModelOther.getBooksUserRequest() != null) {
                            _bookRequestModelOtherArrayList = (ArrayList<BookRequestModel>) _userModelOther.getBooksUserRequest();
                        } else {
                            _bookRequestModelOtherArrayList = new ArrayList<>();
                        }
                        getBookDetail();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            myRef.child(_bookRequestModel.getUserSendRequest().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        _userModelOther = snapshot.getValue(UserModel.class);
                        if (_userModelOther.getNotifyModelList() != null) {
                            _notifyModelOtherArrayList = (ArrayList<NotifyModel>) _userModelOther.getNotifyModelList();
                        } else {
                            _notifyModelOtherArrayList = new ArrayList<>();
                        }
                        if (_userModelOther.getBooksUserRequest() != null) {
                            _bookRequestModelOtherArrayList = (ArrayList<BookRequestModel>) _userModelOther.getBooksUserRequest();
                        } else {
                            _bookRequestModelOtherArrayList = new ArrayList<>();
                        }
                        getBookDetail();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getBookDetail() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        myRef.child(_bookRequestModel.getBookModel().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _bookModel = snapshot.getValue(BookModel.class);
                    reload();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUser() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        _userModel.setBooksUserRequest(_bookRequestModelArrayList);
        _userModel.setNotifyModelList(_notifyModelArrayList);

        myRef.child(_userModel.getUsername()).setValue(_userModel);
    }

    private void updateOtherUser() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        _userModelOther.setBooksUserRequest(_bookRequestModelOtherArrayList);
        _userModelOther.setNotifyModelList(_notifyModelOtherArrayList);

        myRef.child(_userModelOther.getUsername()).setValue(_userModelOther);
    }

    private void updateBook() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        myRef.child(_bookModel.getName()).setValue(_bookModel);
    }

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