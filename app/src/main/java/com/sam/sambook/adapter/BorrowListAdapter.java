package com.sam.sambook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.activity.BorrowDetailActivity;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.BookRequestModel;
import com.sam.sambook.model.NotifyModel;
import com.sam.sambook.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BorrowListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private UserModel _userModel;
    private BookModel _bookModel;
    private ArrayList<UserModel> userModelList;
    private NotifyModel _notifyModel;

    public BorrowListAdapter(Context context, ArrayList<UserModel> userModelList, BookModel bookModel, UserModel userModel) {
        this.context = context;
        this.userModelList = userModelList;
        this._bookModel = bookModel;
        this._userModel = userModel;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_borrower, null);
            viewHolder = new ViewHolder();
            viewHolder.imgBorrower = convertView.findViewById(R.id.img_borrower);
            viewHolder.tvBorrowerName = convertView.findViewById(R.id.tv_borrower_name);
            viewHolder.tvBorrowerUserName = convertView.findViewById(R.id.tv_borrower_username);
            viewHolder.tvBorrowerAddress = convertView.findViewById(R.id.tv_borrower_address);
            viewHolder.btnBorrow = convertView.findViewById(R.id.btn_borrow_book_borrower);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final UserModel userModel = this.userModelList.get(position);
        viewHolder.tvBorrowerName.setText(userModel.getName());
        viewHolder.tvBorrowerUserName.setText(userModel.getUsername());
        viewHolder.tvBorrowerAddress.setText(userModel.getAddress());
//        viewHolder.imgBorrower.setImageResource(userModel.getImgUser());

        viewHolder.btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create notify
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                _notifyModel = new NotifyModel(_userModel.getUsername() + " muốn mượn sách của bạn", _bookModel.getName(), formattedDate);

                Intent intent = new Intent(context, BorrowDetailActivity.class);
                intent.putExtra("SENDER", _userModel);
                intent.putExtra("RECEIVER", userModelList.get(position));
                intent.putExtra("BOOKDETAIL", _bookModel);
                intent.putExtra("NOTIFY", _notifyModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgBorrower;
        TextView tvBorrowerName, tvBorrowerAddress, tvBorrowerUserName;
        Button btnBorrow;
    }
}
