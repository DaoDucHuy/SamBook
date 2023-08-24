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
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class MoreBookAdapter extends RecyclerView.Adapter<MoreBookAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BookModel> bookModelArrayList;

    public MoreBookAdapter(Context context, ArrayList<BookModel> bookModelArrayList) {
        inflater = LayoutInflater.from(context);
        this.bookModelArrayList = bookModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cardview_book_detail, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(bookModelArrayList.get(position).getName());
        holder.author.setText(bookModelArrayList.get(position).getAuthor());
//        new DownloadImageTask(holder.img)
//                .execute(bookModelArrayList.get(position).getImage());
        Picasso.get().load(bookModelArrayList.get(position).getImage()).into(holder.img);
        holder.numbReader.setText(String.valueOf(bookModelArrayList.get(position).getNumbReader()));
        holder.numbBorrower.setText(String.valueOf(bookModelArrayList.get(position).getNumbBorrow()));
        holder.numbRating.setText(String.valueOf(bookModelArrayList.get(position).getNumbRating()));

//        holder.btnAddBookReaded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "gh"+ bookModelArrayList.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView author;
        GifImageView img;
        TextView numbReader;
        TextView numbBorrower;
        TextView numbRating;
//        Button btnAddBookReaded;
//        Button btnBorrowBook;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_title_more_book);
            author = itemView.findViewById(R.id.tv_tacgia);
            img = itemView.findViewById(R.id.img_book_more);
            numbReader = itemView.findViewById(R.id.tv_numb_reader);
            numbBorrower = itemView.findViewById(R.id.tv_numb_borrower);
            numbRating = itemView.findViewById(R.id.tv_numb_rating);
//            btnAddBookReaded = itemView.findViewById(R.id.btn_add_read_book);
//            btnBorrowBook = itemView.findViewById(R.id.btn_borrow_book);

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
