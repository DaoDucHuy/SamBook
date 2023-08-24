package com.sam.sambook.fragmentProfileBook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.adapter.BooksProfileAdapter;
import com.sam.sambook.adapter.ReviewBookProfileAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.Objects;

public class Tab2 extends Fragment {

    private RecyclerView recyclerViewReviewProfile;
    private ArrayList<BookModel> booksReviewArrayList;
    private ReviewBookProfileAdapter reviewBookProfileAdapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserModel _userModel;
    private ArrayList<BookModel> _allBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_fragment_2, container, false);

        getUser();
        getBook();

        recyclerViewReviewProfile = root.findViewById(R.id.rcv_review_profile);
        booksReviewArrayList = new ArrayList<>();
        reviewBookProfileAdapter = new ReviewBookProfileAdapter(getActivity().getApplicationContext(), booksReviewArrayList);
        recyclerViewReviewProfile.setAdapter(reviewBookProfileAdapter);
        recyclerViewReviewProfile.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerViewReviewProfile.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(),recyclerViewReviewProfile, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return root;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getUser();
//            getBook();
//
////            booksReviewArrayList = new ArrayList<>();
//            reviewBookProfileAdapter = new ReviewBookProfileAdapter(getActivity().getApplicationContext(), booksReviewArrayList);
//            recyclerViewReviewProfile.setAdapter(reviewBookProfileAdapter);
//        }
//    }

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

        Log.d("User tab 2", _userModel.getUsername());
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
                getUserBookReview();
                reviewBookProfileAdapter = new ReviewBookProfileAdapter(getActivity().getApplicationContext(), booksReviewArrayList);
                recyclerViewReviewProfile.setAdapter(reviewBookProfileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserBookReview() {
        booksReviewArrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users").child(_userModel.getUsername()).child("booksUserReview");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookModel bookModel;
                bookModel = snapshot.getValue(BookModel.class);
                for (int i = 0; i < _allBooks.size(); i++) {
                    if (Objects.equals(_allBooks.get(i).getName(), bookModel.getName())) {
                        booksReviewArrayList.add(_allBooks.get(i));
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
                if (booksReviewArrayList.size() != 0) {
                    Log.d("Books tab 2", String.valueOf(booksReviewArrayList.size()));
                    reviewBookProfileAdapter = new ReviewBookProfileAdapter(getActivity().getApplicationContext(), booksReviewArrayList);
                    recyclerViewReviewProfile.setAdapter(reviewBookProfileAdapter);
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
