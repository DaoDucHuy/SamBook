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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BookModel> bookModelArrayList;
    private List<BookModel> bookModelList; //add all book here for filter book

    public BookAdapter(Context context, ArrayList<BookModel> bookModelArrayList) {
        inflater = LayoutInflater.from(context);
        this.bookModelArrayList = bookModelArrayList;
        this.bookModelList = new ArrayList<BookModel>();
        this.bookModelList.addAll(bookModelArrayList);
    }

    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cardview_book, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, int position) {
        holder.name.setText(bookModelArrayList.get(position).getName());
        holder.author.setText(bookModelArrayList.get(position).getAuthor());
//        new DownloadImageTask(holder.img)
//                .execute(bookModelArrayList.get(position).getImage());
        Picasso.get().load(bookModelArrayList.get(position).getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView author;
        GifImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.book_name_id);
            author = itemView.findViewById(R.id.book_author_id);
            img = itemView.findViewById(R.id.book_img_id);
        }
    }

    // Filter Search
    public void filterCategory(String charText) {
        bookModelArrayList.clear();
        if (charText.equals("Tất cả")) {
            bookModelArrayList.addAll(bookModelList);
        } else {
            for (BookModel bk : bookModelList) {
                if (bk.getTypeBook().equals(charText)) {
                    bookModelArrayList.add(bk);
                }
            }
        }

        notifyDataSetChanged();
    }

    // Combination Filter Search
    public void filterCombination(String charText, String category) {
        bookModelArrayList.clear();

        ArrayList<BookModel> filterList = new ArrayList<>();

        if (category.equals("Tất cả")) {
            filterList.addAll(bookModelList);
        } else {
            for (BookModel bk : bookModelList) {
                if (bk.getTypeBook().equals(category)) {
                    filterList.add(bk);
                }
            }
        }

        String s = charText;
        s = normalize(s);

        Log.d("Search text", s);

        charText = normalize(charText);
        if (charText.length() == 0) {
            bookModelArrayList.addAll(filterList);
        }
        else {
            for (BookModel bk : filterList) {
                String bookTitle = normalize(bk.getName());
                String bookAuthor = normalize(bk.getAuthor());
                if (bookTitle.contains(charText) && !bookModelArrayList.contains(bk)) {
                    bookModelArrayList.add(bk);
                }
                if (bookAuthor.contains(charText) && !bookModelArrayList.contains(bk)) {
                    bookModelArrayList.add(bk);
                }
            }
        }

        notifyDataSetChanged();
    }

    private static String normalize(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.toLowerCase(Locale.getDefault());
        return s;
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
