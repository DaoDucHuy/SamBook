package com.sam.sambook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.BookReviewedModel;
import com.sam.sambook.model.TempBookReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewBookDetailAdapter extends RecyclerView.Adapter<ReviewBookDetailAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<BookReviewedModel> usersReviewArrayList;

    public ReviewBookDetailAdapter(Context context, List<BookReviewedModel> usersReviewArrayList) {
        inflater = LayoutInflater.from(context);
        this.usersReviewArrayList = usersReviewArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cardview_review_book_detail, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.user_name.setText(usersReviewArrayList.get(position).getUserReviewBook().getName());
        holder.review_content.setText(usersReviewArrayList.get(position).getReviewContent());
        holder.user_image.setImageResource(usersReviewArrayList.get(position).getUserReviewBook().getImgUser());
        holder.ratingBar.setRating((float) usersReviewArrayList.get(position).getNumbRating());
    }

    @Override
    public int getItemCount() {
        return usersReviewArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView user_name;
        TextView review_content;
        ImageView user_image;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.book_detail_review_user_name);
            review_content = itemView.findViewById(R.id.book_detail_review_content);
            user_image = itemView.findViewById(R.id.img_book_detail_review_profile);
            ratingBar = itemView.findViewById(R.id.book_detail_review_rating_bar);
        }
    }
}
