package com.sam.sambook.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.activity.BookDetailActivity;
import com.sam.sambook.adapter.BookAdapter;
import com.sam.sambook.manager.BookManager;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.TypeBook;
import com.sam.sambook.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private SearchView searchViewBook;
    private RecyclerView recyclerSearchBook;
    private ArrayList<BookModel> bookModelArrayList;
    private BookAdapter bookAdapter;
    private Spinner dropDown;
    private ArrayList<TypeBook> typeBookArrayList;


    private ArrayList<BookModel> _allBooks = new ArrayList<>();
    private ArrayList<TypeBook> _allTypes = new ArrayList<>();
    private UserModel _userModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //init
        getUser();
        getBook();
        getTypeBook();


        recyclerSearchBook = root.findViewById(R.id.rcv_search_book);
        bookModelArrayList = new ArrayList<>();
        bookAdapter = new BookAdapter(getActivity().getApplicationContext(), bookModelArrayList);
        recyclerSearchBook.setAdapter(bookAdapter);
        recyclerSearchBook.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));
        recyclerSearchBook.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerSearchBook, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                intent.putExtra("USER", _userModel);
                intent.putExtra("BOOKDETAIL", bookModelArrayList.get(position));
                intent.putExtra("FROM_ACTIVITY", "Main");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        searchViewBook = root.findViewById(R.id.search_book);
        searchViewBook.setOnQueryTextListener(this);

        dropDown = root.findViewById(R.id.search_spinner);
        typeBookArrayList = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        if (typeBookArrayList != null) {
            for (int i = 0; i < typeBookArrayList.size(); i++) {
                items.add(typeBookArrayList.get(i).getNameType());
            }
            items.add(0, "Tất cả");
        }
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(dropDownAdapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookAdapter.filterCategory(dropDown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        //bookAdapter.filter(text);
        bookAdapter.filterCombination(text, dropDown.getSelectedItem().toString());
        return false;
    }

    private void getUser() {
        _userModel = (UserModel) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("USER");

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
                bookModelArrayList = new ArrayList<>(_allBooks);
                bookAdapter = new BookAdapter(getActivity().getApplicationContext(), bookModelArrayList);
                recyclerSearchBook.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTypeBook() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("TypeBook");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TypeBook typeBook;
                typeBook = snapshot.getValue(TypeBook.class);
                _allTypes.add(typeBook);
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
                typeBookArrayList = new ArrayList<>(_allTypes);
                ArrayList<String> items = new ArrayList<>();
                if (typeBookArrayList != null) {
                    for (int i = 0; i < typeBookArrayList.size(); i++) {
                        items.add(typeBookArrayList.get(i).getNameType());
                    }
                    items.add(0, "Tất cả");
                }
                ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                dropDown.setAdapter(dropDownAdapter);
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