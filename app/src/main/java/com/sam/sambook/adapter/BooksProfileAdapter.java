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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class BooksProfileAdapter extends RecyclerView.Adapter<BooksProfileAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BookModel> booksProfileArrayList;

    public BooksProfileAdapter(Context context, ArrayList<BookModel> booksProfileArrayList) {
        inflater = LayoutInflater.from(context);
        this.booksProfileArrayList = booksProfileArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_books_profile, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name_book_profile.setText(booksProfileArrayList.get(position).getName());
        holder.author_books_profile.setText(booksProfileArrayList.get(position).getAuthor());
//        new DownloadImageTask(holder.img_books_profile)
//                .execute(booksProfileArrayList.get(position).getImage());
        Picasso.get().load(booksProfileArrayList.get(position).getImage()).into(holder.img_books_profile);
    }

    @Override
    public int getItemCount() {
        return booksProfileArrayList.size();
    }

    public void clear(){
        booksProfileArrayList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<BookModel> bookModelList){
//        booksProfileArrayList.addAll(bookModelList);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_book_profile;
        TextView author_books_profile;
        GifImageView img_books_profile;
        TextView numbreader;
        TextView numbborrower;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_book_profile = itemView.findViewById(R.id.tv_title_books_profile);
            author_books_profile = itemView.findViewById(R.id.tv_tacgia_books_profile);
            img_books_profile = itemView.findViewById(R.id.img_books_profile);
//            numbreader = itemView.findViewById(R.id.tv_numb_reader_books_profile);
//            numbborrower = itemView.findViewById(R.id.tv_numb_borrower_books_request);

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
