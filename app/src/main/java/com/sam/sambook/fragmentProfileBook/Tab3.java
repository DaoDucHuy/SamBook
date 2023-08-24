package com.sam.sambook.fragmentProfileBook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.activity.BookDetailActivity;
import com.sam.sambook.activity.RequestDetailActivity;
import com.sam.sambook.adapter.BookUserRequestAdapter;
import com.sam.sambook.model.BookRequestModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Tab3 extends Fragment {

    private RecyclerView recyclerViewBookRequest;
    private ArrayList<BookRequestModel> bookRequestModelList;
    private BookUserRequestAdapter bookUserRequestAdapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserModel _userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        getUser();

        if (_userModel.getBooksUserRequest() != null) {
            bookRequestModelList = (ArrayList<BookRequestModel>) _userModel.getBooksUserRequest();

        } else {
            bookRequestModelList = new ArrayList<>();
        }

        recyclerViewBookRequest = view.findViewById(R.id.rcv_book_user_request);
        bookUserRequestAdapter = new BookUserRequestAdapter(getActivity().getApplicationContext(), (ArrayList<BookRequestModel>) bookRequestModelList, _userModel);
        recyclerViewBookRequest.setAdapter(bookUserRequestAdapter);
        recyclerViewBookRequest.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerViewBookRequest.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerViewBookRequest, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RequestDetailActivity.class);
                intent.putExtra("USER", _userModel);
                intent.putExtra("REQUEST", bookRequestModelList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;

    }

    private void getUser() {
        _userModel = (UserModel) getActivity().getIntent().getSerializableExtra("USER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _userModel = snapshot.getValue(UserModel.class);
                    if (_userModel.getBooksUserRequest() != null) {
                        bookRequestModelList = (ArrayList<BookRequestModel>) _userModel.getBooksUserRequest();

                    } else {
                        bookRequestModelList = new ArrayList<>();
                    }
                    bookUserRequestAdapter = new BookUserRequestAdapter(getActivity().getApplicationContext(), (ArrayList<BookRequestModel>) bookRequestModelList, _userModel);
                    recyclerViewBookRequest.setAdapter(bookUserRequestAdapter);
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
