package com.sam.sambook.fragmentBookDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.sam.sambook.activity.EditReviewActivity;
import com.sam.sambook.activity.ReviewContentActivity;
import com.sam.sambook.adapter.ReviewBookDetailAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.BookReviewedModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentReview extends Fragment {

    private RecyclerView recyclerViewBookDetailReview;
    private ArrayList<BookReviewedModel> bookReviewedModelList;
    private ArrayList<BookModel> reviewedBookModelList;
    private ReviewBookDetailAdapter reviewBookDetailAdapter;
    private RatingBar activeRatingBar;
    private TextView textViewEditReview;

    private BookModel _bookModel;
    private UserModel _userModel;

    private boolean addingReview = false; //Added review content checker
    private static final int REQUEST_CODE = 0x9345;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_fragment_book_detail_review, container, false);

//        _getUserLocal();

        recyclerViewBookDetailReview = root.findViewById(R.id.rcv_book_detail_review);
        activeRatingBar = root.findViewById(R.id.book_detail_review_rating_bar_active);
        textViewEditReview = root.findViewById(R.id.book_detail_edit_review);

        getUser();
        getBookDetail();


        bookReviewedModelList = (ArrayList<BookReviewedModel>) _bookModel.getBookReviewedModel();
        if (bookReviewedModelList == null) {
            bookReviewedModelList = new ArrayList<>();
        }
        reviewBookDetailAdapter = new ReviewBookDetailAdapter(getActivity().getApplicationContext(), bookReviewedModelList);
        recyclerViewBookDetailReview.setAdapter(reviewBookDetailAdapter);
        recyclerViewBookDetailReview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        for (int i = 0; i < bookReviewedModelList.size(); i++) {
            if (Objects.equals(_userModel.getUsername(), bookReviewedModelList.get(i).getUserReviewBook().getUsername())
                    && !Objects.equals(bookReviewedModelList.get(i).getReviewContent(), "")) {
                textViewEditReview.setText(R.string.edit_reviewed);
                break;
            }
        }

        recyclerViewBookDetailReview.addOnItemTouchListener(new FragmentReview.RecyclerTouchListener(getActivity().getApplicationContext(),
                recyclerViewBookDetailReview, new FragmentReview.RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast.makeText(getActivity().getApplicationContext(), "Rating: " + bookReviewedModelList.get(position).getNumbRating(), Toast.LENGTH_SHORT).show();
                if (!Objects.equals(bookReviewedModelList.get(position).getReviewContent(), null)) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ReviewContentActivity.class);
                    intent.putExtra("REVIEW", bookReviewedModelList.get(position));
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //Handle result from changing the score on RatingBar
        activeRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(activeRatingBar.getRating()) , Toast.LENGTH_SHORT).show();

                if (!addingReview) {
                    int indexLocation = -1;
                    // check if the reviewModel was existed
                    for (int i = 0; i < bookReviewedModelList.size(); i++) {
                        if (Objects.equals(_userModel.getUsername(), bookReviewedModelList.get(i).getUserReviewBook().getUsername())) {
                            indexLocation = i;
                            break;
                        }
                    }
                    // if existed
                    if (indexLocation != -1) {
                        UserModel userModel = new UserModel(_userModel.getUsername(), _userModel.getPassword(), _userModel.getName(), _userModel.getAddress());
                        BookReviewedModel bookReviewedModel = new BookReviewedModel(_bookModel.getName(), userModel, bookReviewedModelList.get(indexLocation).getReviewContent(), activeRatingBar.getRating());
                        bookReviewedModelList.remove(indexLocation);
                        bookReviewedModelList.add(0, bookReviewedModel);
                        addingReview = true;
                    }
                    // if not existed
                    else {
                        UserModel userModel = new UserModel(_userModel.getUsername(), _userModel.getPassword(), _userModel.getName(), _userModel.getAddress());
                        BookReviewedModel bookReviewedModel = new BookReviewedModel(_bookModel.getName(), userModel, "", activeRatingBar.getRating());
                        bookReviewedModelList.add(0, bookReviewedModel);
                        addingReview = true;
                    }
                } else {
                    UserModel userModel = new UserModel(_userModel.getUsername(), _userModel.getPassword(), _userModel.getName(), _userModel.getAddress());
                    BookReviewedModel bookReviewedModel = new BookReviewedModel(_bookModel.getName(), userModel, bookReviewedModelList.get(0).getReviewContent(), activeRatingBar.getRating());
                    bookReviewedModelList.remove(0);
                    bookReviewedModelList.add(0, bookReviewedModel);
                }
                reviewBookDetailAdapter.notifyDataSetChanged();

                updateReview();
            }
        });

        textViewEditReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "edit review" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), EditReviewActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return root;
    }

    //Handle the result from EditReview activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String reviewContent = data.getStringExtra("REVIEWCONTENT");
                float ratingScore = data.getFloatExtra("RATINGSCORE", 0);
//                Toast.makeText(getActivity().getApplicationContext(), reviewContent , Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(ratingScore) , Toast.LENGTH_SHORT).show();

                UserModel userModel = new UserModel(_userModel.getUsername(), _userModel.getPassword(), _userModel.getName(), _userModel.getAddress());
                BookReviewedModel bookReviewedModel = new BookReviewedModel(_bookModel.getName(), userModel, reviewContent, ratingScore);
                if (!addingReview) {
                    int indexLocation = -1;
                    for (int i = 0; i < bookReviewedModelList.size(); i++) {
                        if (Objects.equals(bookReviewedModel.getUserReviewBook().getUsername(), bookReviewedModelList.get(i).getUserReviewBook().getUsername())) {
                            indexLocation = i;
                            break;
                        }
                    }
                    if (indexLocation != -1) {
                        bookReviewedModelList.remove(indexLocation);
                        bookReviewedModelList.add(0, bookReviewedModel);
                        textViewEditReview.setText(R.string.edit_reviewed);
                        addingReview = true;
                    } else {
                        bookReviewedModelList.add(0, bookReviewedModel);
                        textViewEditReview.setText(R.string.edit_reviewed);
                        addingReview = true;
                    }
                } else {
                    bookReviewedModelList.remove(0);
                    bookReviewedModelList.add(0, bookReviewedModel);
                    textViewEditReview.setText(R.string.edit_reviewed);
                }
                reviewBookDetailAdapter.notifyDataSetChanged();

                updateReview();
            }
        }
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
    }

//    private void _getUserLocal() {
//        try {
//            InputStream is = getActivity().getApplicationContext().openFileInput("USERSAMBOOK.txt");
//            int size = is.available();
//            byte data[] = new byte[size];
//            is.read(data);
//            is.close();
//            String s = new String(data);
//            Gson gson = new Gson();
//            _userModelLocal = gson.fromJson(s, UserModel.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Log.d("BookDetail", _bookModel.getName());
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

        reviewedBookModelList = (ArrayList<BookModel>) _userModel.getBooksUserReview();

        Log.d("User", _userModel.getUsername());
    }

    private void updateReview() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        _bookModel.setBookReviewedModel(bookReviewedModelList);
        _bookModel.ratingCal();

        myRef.child("Books").child(_bookModel.getName()).setValue(_bookModel);

        RatingBar ratingBar = getActivity().findViewById(R.id.book_detail_rating_bar);
        ratingBar.setRating(_bookModel.getNumbRating());

        TextView rating = getActivity().findViewById(R.id.book_detail_rating);
        rating.setText(String.valueOf(_bookModel.getNumbRating()));

        if (reviewedBookModelList == null) {
            reviewedBookModelList = new ArrayList<>();

            int indexLocation = -1;
            for (int i = 0; i < reviewedBookModelList.size(); i++) {
                if (reviewedBookModelList.get(i).getName().equals(_bookModel.getName())) {
                    indexLocation = i;
                    //Huy test merge
                    break;
                }
            }
            if (indexLocation != -1) {
                reviewedBookModelList.remove(indexLocation);
            }
            BookModel bookModel = new BookModel(_bookModel.getName(), _bookModel.getAuthor(), _bookModel.getImage(), _bookModel.getNumbReader(), _bookModel.getNumbBorrow(),
                    _bookModel.getDescription(), _bookModel.getNumbRating(), _bookModel.getTypeBook());
            reviewedBookModelList.add(bookModel);
        } else {
            int indexLocation = -1;
            for (int i = 0; i < reviewedBookModelList.size(); i++) {
                if (reviewedBookModelList.get(i).getName().equals(_bookModel.getName())) {
                    indexLocation = i;
                    break;
                }
            }
            if (indexLocation != -1) {
                reviewedBookModelList.remove(indexLocation);
            }
            BookModel bookModel = new BookModel(_bookModel.getName(), _bookModel.getAuthor(), _bookModel.getImage(), _bookModel.getNumbReader(), _bookModel.getNumbBorrow(),
                    _bookModel.getDescription(), _bookModel.getNumbRating(), _bookModel.getTypeBook());
            reviewedBookModelList.add(bookModel);
        }
        _userModel.setBooksUserReview(reviewedBookModelList);

        myRef.child("Users").child(_userModel.getUsername()).setValue(_userModel);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragmentReview.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentReview.RecyclerTouchListener.ClickListener clickListener) {
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
