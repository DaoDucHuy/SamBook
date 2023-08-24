package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.adapter.BookAdapter;
import com.sam.sambook.adapter.MoreBookAdapter;
import com.sam.sambook.manager.BookManager;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;
import com.sam.sambook.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListMoreBookActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerViewMoreBook;
    private ArrayList<BookModel> bookModelArrayList;
    private MoreBookAdapter moreBookAdapter;

    private ArrayList<BookModel> _allBooks = new ArrayList<>();
    private UserModel _userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_more_book);
        setTitle("Khám phá sách nổi bật");

        //Show back button on action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getUser();
        getBook();

        recyclerViewMoreBook = findViewById(R.id.rcv_more_book);
        bookModelArrayList = new ArrayList<>();
        moreBookAdapter = new MoreBookAdapter(getApplicationContext(), bookModelArrayList);
        recyclerViewMoreBook.setAdapter(moreBookAdapter);
        recyclerViewMoreBook.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewMoreBook.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewMoreBook, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", bookModelArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "ListMoreBook");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

    private void getTrendingBook() {
        ArrayList<BookModel> allBooks = new ArrayList<>(_allBooks);
        ArrayList<BookModel> trendingBooks = new ArrayList<>();

        if (allBooks.size() > 20) {
            int count = 0;
            int position;
            while (count < 20) {
                position = 0;
                int max = 0;
                for (int i = 0; i < allBooks.size(); i++) {
                    if (max <= allBooks.get(i).getNumbBorrow()) {
                        max = allBooks.get(i).getNumbBorrow();
                        position = i;
                    }
                }
                trendingBooks.add(allBooks.get(position));
                allBooks.remove(position);
                count++;
            }
        }
        else {
            trendingBooks = allBooks;
        }
        bookModelArrayList = trendingBooks;
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

    private void getBook() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Books");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookModel bookModel;
                bookModel = snapshot.getValue(BookModel.class);
                _allBooks.add(bookModel);
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
                getTrendingBook();
                moreBookAdapter = new MoreBookAdapter(getApplicationContext(), bookModelArrayList);
                recyclerViewMoreBook.setAdapter(moreBookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListMoreBookActivity.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListMoreBookActivity.RecyclerTouchListener.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }
    }
}