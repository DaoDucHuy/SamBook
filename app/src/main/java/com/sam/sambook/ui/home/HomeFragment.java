package com.sam.sambook.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.sam.sambook.activity.BookDetailActivity;
import com.sam.sambook.activity.ListMoreBookActivity;
import com.sam.sambook.adapter.BookAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.TypeBook;
import com.sam.sambook.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RecyclerView recyclerReleaseBook, recyclerTrendingBook, recyclerRecommendBook;
    private ArrayList<BookModel> newReleaseBookArrayList;
    private BookAdapter newReleaseBookAdapter;
    private ArrayList<BookModel> trendingBookArrayList;
    private BookAdapter trendingBookAdapter;
    private ArrayList<BookModel> recommendBookArrayList;
    private BookAdapter recommendBookAdapter;

    private TextView tvUserName;

    private ArrayList<BookModel> _allBooks = new ArrayList<>();
    private UserModel _userModel;

    //view action
    private TextView tv_more_hotbook;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        //init
        getUser();
        getBook();

        tvUserName = root.findViewById(R.id.tv_user_name);
        tvUserName.setText(getResources().getString(R.string.home_greeting) + " " + _userModel.getUsername());

        tv_more_hotbook = root.findViewById(R.id.tv_more_hotbook);
        tv_more_hotbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(), "more hot book", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), ListMoreBookActivity.class);
                intent.putExtra("USER", _userModel);
//                intent.putExtra("ALLBOOK", (Serializable) _allBook);
                startActivity(intent);
            }
        });

        recyclerTrendingBook = root.findViewById(R.id.rcv_hot_book);
        trendingBookArrayList = new ArrayList<>();
        trendingBookAdapter = new BookAdapter(getActivity().getApplicationContext(), trendingBookArrayList);
        recyclerTrendingBook.setAdapter(trendingBookAdapter);
        recyclerTrendingBook.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerTrendingBook.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerTrendingBook, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast.makeText(getActivity().getApplicationContext(), "book " + trendingBookArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", trendingBookArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "Main");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerReleaseBook = root.findViewById(R.id.rcv_new_releases_book);
        newReleaseBookArrayList = new ArrayList<>();
        newReleaseBookAdapter = new BookAdapter(getActivity().getApplicationContext(), newReleaseBookArrayList);
        recyclerReleaseBook.setAdapter(newReleaseBookAdapter);
        recyclerReleaseBook.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerReleaseBook.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerReleaseBook, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", newReleaseBookArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "Main");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerRecommendBook = root.findViewById(R.id.rcv_new_selected_book);

        recommendBookArrayList = new ArrayList<>();
        recommendBookAdapter = new BookAdapter(getActivity().getApplicationContext(), recommendBookArrayList);
        recyclerRecommendBook.setAdapter(recommendBookAdapter);
        recyclerRecommendBook.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRecommendBook.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerRecommendBook, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", recommendBookArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "Main");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        even();

        return root;
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

        Log.d("User", _userModel.getUsername());
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
                getTrendingBook();
                trendingBookAdapter = new BookAdapter(getActivity().getApplicationContext(), trendingBookArrayList);
                recyclerTrendingBook.setAdapter(trendingBookAdapter);
                getNewReleaseBook();
                newReleaseBookAdapter = new BookAdapter(getActivity().getApplicationContext(), newReleaseBookArrayList);
                recyclerReleaseBook.setAdapter(newReleaseBookAdapter);
                getRecommendBook();
                recommendBookAdapter = new BookAdapter(getActivity().getApplicationContext(), recommendBookArrayList);
                recyclerRecommendBook.setAdapter(recommendBookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTrendingBook() {
        ArrayList<BookModel> allBooks = new ArrayList<>(_allBooks);
        ArrayList<BookModel> trendingBooks = new ArrayList<>();

        if (allBooks.size() > 10) {
            int count = 0;
            int position;
            while (count < 10) {
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
        trendingBookArrayList = trendingBooks;
    }

    private void getNewReleaseBook() {
        ArrayList<BookModel> allBooks = new ArrayList<>(_allBooks);
        ArrayList<BookModel> newReleaseBooks = new ArrayList<>();

        if (allBooks.size() > 10) {
            int count = 0;
            for (int i = allBooks.size() - 1; i > 0; i--) {
                if (count < 10) {
                    newReleaseBooks.add(allBooks.get(i));
                    count++;
                }
                else {
                    break;
                }
            }
        }
        else {
            newReleaseBooks = allBooks;
        }
        newReleaseBookArrayList = newReleaseBooks;
    }

    private void getRecommendBook() {
        recommendBookArrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users").child(_userModel.getUsername()).child("booksUserRecommend");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookModel bookModel;
                bookModel = snapshot.getValue(BookModel.class);
                for (int i = 0; i < _allBooks.size(); i++) {
                    if (Objects.equals(_allBooks.get(i).getName(), bookModel.getName())) {
                        recommendBookArrayList.add(_allBooks.get(i));
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
                if (recommendBookArrayList.size() != 0) {
                    Log.d("Books", String.valueOf(recommendBookArrayList.size()));
                    recommendBookAdapter = new BookAdapter(getActivity().getApplicationContext(), recommendBookArrayList);
                    recyclerRecommendBook.setAdapter(recommendBookAdapter);
                } else {
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
                        while (trendingBooks.size() > 10) {
                            trendingBooks.remove(0);
                        }
                    }
                    else {
                        trendingBooks = allBooks;
                    }
                    recommendBookArrayList = trendingBooks;
                    recommendBookAdapter = new BookAdapter(getActivity().getApplicationContext(), recommendBookArrayList);
                    recyclerRecommendBook.setAdapter(recommendBookAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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