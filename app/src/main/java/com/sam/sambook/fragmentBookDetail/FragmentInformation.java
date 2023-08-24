package com.sam.sambook.fragmentBookDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;

public class FragmentInformation extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextView description_content;
    private BookModel _bookModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.tab_fragment_bookdetail_details, container, false);
        View root = inflater.inflate(R.layout.tab_fragment_book_detail_information, container, false);

        // Receive book data and load on view
        description_content = root.findViewById(R.id.book_detail_description_content);
        getBookDetail();
        if (_bookModel != null) {
            description_content.setText(_bookModel.getDescription());
        }

        return root;
    }

    private void getBookDetail() {
        _bookModel = (BookModel) getActivity().getIntent().getSerializableExtra("BOOKDETAIL");

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

        Log.d("BookDetailInformation", _bookModel.getName());
    }
}
