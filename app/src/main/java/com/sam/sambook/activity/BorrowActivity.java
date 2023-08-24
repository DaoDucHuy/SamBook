package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.adapter.BorrowListAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.Objects;

public class BorrowActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserModel _userModel;
    private BookModel _bookModel;
    private ArrayList<UserModel> userModelArrayList;
    private ArrayList<UserModel> userAvailableArrayList;

    private UserModel userModelTemp;

    private ListView listViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        setTitle("Danh sách người dùng");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getUser();
        getBookDetail();

        if (_bookModel.getUserModelList() != null) {
            userModelArrayList = (ArrayList<UserModel>) _bookModel.getUserModelList();
            ArrayList<UserModel> userModelArrayListTemp = new ArrayList<>();
            ArrayList<String> userNameArrayListTemp = new ArrayList<>();
            for (UserModel userModel : userModelArrayList) {
                if (!Objects.equals(userModel.getUsername(), _userModel.getUsername()) && !userNameArrayListTemp.contains(userModel.getUsername())) {
                    userModelArrayListTemp.add(userModel);
                    userNameArrayListTemp.add(userModel.getUsername());
                }
            }
            userAvailableArrayList = new ArrayList<>();
            getUserModelArrayList(userNameArrayListTemp);
            userModelArrayList = userAvailableArrayList;
        } else {
            userModelArrayList = new ArrayList<>();
        }

        listViewUser = findViewById(R.id.list_borrower);
        BorrowListAdapter borrowListAdapter = new BorrowListAdapter(getApplicationContext(), userModelArrayList, _bookModel, _userModel);
        listViewUser.setAdapter(borrowListAdapter);
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    }

    private void getUserModelArrayList(final ArrayList<String> userNameArrayList) {
        final ArrayList<UserModel> userModelArrayListTemp = new ArrayList<>();
        final ArrayList<UserModel> userModelArrayListTemp2 = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userNameArrayList.contains(userModel.getUsername())) {
                    userModelArrayListTemp.add(userModel);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < userModelArrayListTemp.size(); i++) {
                    for (int j = 0; j < userModelArrayListTemp.get(i).getBooksUser().size(); j++) {
                        if (Objects.equals(userModelArrayListTemp.get(i).getBooksUser().get(j).getName(), _bookModel.getName()) && userModelArrayListTemp.get(i).getBooksUser().get(j).getStatus()) {
                            userModelArrayListTemp2.add(userModelArrayListTemp.get(i));
                            break;
                        }
                    }
                }

                userAvailableArrayList = userModelArrayListTemp2;
                userModelArrayList = userAvailableArrayList;
                BorrowListAdapter borrowListAdapter = new BorrowListAdapter(getApplicationContext(), userModelArrayList, _bookModel, _userModel);
                listViewUser.setAdapter(borrowListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}