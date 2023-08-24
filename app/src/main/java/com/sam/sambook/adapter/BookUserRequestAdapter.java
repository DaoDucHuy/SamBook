package com.sam.sambook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.sambook.R;
import com.sam.sambook.model.BookRequestModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.Objects;

public class BookUserRequestAdapter extends RecyclerView.Adapter<BookUserRequestAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<BookRequestModel> bookRequestModels;
    private UserModel _userModel;

    public BookUserRequestAdapter(Context context, ArrayList<BookRequestModel> bookRequestModels, UserModel userModel) {
        inflater = LayoutInflater.from(context);
        this.bookRequestModels = bookRequestModels;
        this._userModel = userModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_book_request, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (Objects.equals(bookRequestModels.get(position).getUserSendRequest().getUsername(), _userModel.getUsername())) {
            holder.username.setText(bookRequestModels.get(position).getUserReceiveRequest().getUsername());
            holder.title.setText("Đã nhận yêu cầu của bạn cho việc mượn");
        } else {
            holder.username.setText(bookRequestModels.get(position).getUserSendRequest().getUsername());
            holder.title.setText("Đã gửi yêu cầu cho việc mượn");
        }
        holder.bookTitle.setText(bookRequestModels.get(position).getBookModel().getName());
        if (bookRequestModels.get(position).getStatus() != null) {
            switch (bookRequestModels.get(position).getStatus()) {
                case "pending":
                    holder.status.setText("Đang chờ chấp thuận");
                    break;
                case "contacting":
                    holder.status.setText("Đang liên lạc");
                    break;
                case "lending":
                    holder.status.setText("Đang cho mượn");
                    break;
                case "returning":
                    holder.status.setText("Đã trả");
                    break;
                case "lost":
                    holder.status.setText("Đã thất lạc");
                case "canceled":
                    holder.status.setText("Đã hủy");
            }
        } else {
            holder.status.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return bookRequestModels.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView title;
        TextView bookTitle;
        TextView status;
        ImageView userImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.item_book_request_user_username);
            title = itemView.findViewById(R.id.item_book_request_title);
            bookTitle = itemView.findViewById(R.id.item_book_request_book_title);
            status = itemView.findViewById(R.id.item_book_request_status);
            userImage = itemView.findViewById(R.id.item_book_request_user_image);
        }
    }
}
