package com.sam.sambook.fragmentProfileBook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.activity.BookDetailActivity;
import com.sam.sambook.activity.ListMoreBookActivity;
import com.sam.sambook.adapter.BookAdapter;
import com.sam.sambook.adapter.BooksProfileAdapter;
import com.sam.sambook.manager.BookManager;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tab1 extends Fragment {

    private RecyclerView recyclerViewBooksProfile;
    private ArrayList<BookModel> booksProfileArrayList;
    private BooksProfileAdapter booksProfileAdapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserModel _userModel;
    private ArrayList<BookModel> _allBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_fragment_1, container, false);

        getUser();
        getBook();

        recyclerViewBooksProfile = root.findViewById(R.id.rcv_books_profile);
        booksProfileArrayList = new ArrayList<>();
        booksProfileAdapter = new BooksProfileAdapter(getActivity().getApplicationContext(), booksProfileArrayList);
        recyclerViewBooksProfile.setAdapter(booksProfileAdapter);
        recyclerViewBooksProfile.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerViewBooksProfile.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerViewBooksProfile, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", booksProfileArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "Main");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Nhan Intent, va dc khoi tao trc onCreateView
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getUser() {
        _userModel = (UserModel) getActivity().getIntent().getSerializableExtra("USER");

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

        Log.d("User tab 1", _userModel.getUsername());
    }

    private void getBook() {
        _allBooks = new ArrayList<>();

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
                getUserBook();
                booksProfileAdapter = new BooksProfileAdapter(getActivity().getApplicationContext(), booksProfileArrayList);
                recyclerViewBooksProfile.setAdapter(booksProfileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserBook() {
        booksProfileArrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users").child(_userModel.getUsername()).child("booksUser");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookModel bookModel;
                bookModel = snapshot.getValue(BookModel.class);
                for (int i = 0; i < _allBooks.size(); i++) {
                    if (Objects.equals(_allBooks.get(i).getName(), bookModel.getName())) {
                        booksProfileArrayList.add(_allBooks.get(i));
                    }
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
                if (booksProfileArrayList.size() != 0) {
                    Log.d("Books tab 1", String.valueOf(booksProfileArrayList.size()));
                    booksProfileAdapter = new BooksProfileAdapter(getActivity().getApplicationContext(), booksProfileArrayList);
                    recyclerViewBooksProfile.setAdapter(booksProfileAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerTouchListener.ClickListener clickListener) {
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
