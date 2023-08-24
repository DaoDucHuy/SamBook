package com.sam.sambook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class ReviewBookProfileAdapter extends RecyclerView.Adapter<ReviewBookProfileAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BookModel> booksProfileArrayList;

    public ReviewBookProfileAdapter(Context context, ArrayList<BookModel> booksProfileArrayList) {
        inflater = LayoutInflater.from(context);
        this.booksProfileArrayList = booksProfileArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review_profile, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_review_profile.setText(booksProfileArrayList.get(position).getName());
        holder.author_review_profile.setText(booksProfileArrayList.get(position).getAuthor());
//        new DownloadImageTask(holder.img_review_profile)
//                .execute(booksProfileArrayList.get(position).getImage());
        Picasso.get().load(booksProfileArrayList.get(position).getImage()).into(holder.img_review_profile);
        holder.numbRating.setRating((float) booksProfileArrayList.get(position).getNumbRating());
    }

    @Override
    public int getItemCount() {
        return booksProfileArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_review_profile;
        TextView author_review_profile;
        GifImageView img_review_profile;
        RatingBar numbRating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_review_profile = itemView.findViewById(R.id.tv_title_review_profile);
            author_review_profile = itemView.findViewById(R.id.tv_tacgia_review_profile);
            img_review_profile = itemView.findViewById(R.id.img_review_profile);
            numbRating = itemView.findViewById(R.id.rating_bar_review_profile);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        GifImageView imageView;

        public DownloadImageTask(GifImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
