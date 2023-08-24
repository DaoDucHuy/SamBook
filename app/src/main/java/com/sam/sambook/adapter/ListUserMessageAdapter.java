package com.sam.sambook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sam.sambook.R;
import com.sam.sambook.activity.ListMessengerActivity;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;

public class ListUserMessageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ListMessengerActivity.ChatItem> chatItemArrayList;

    public ListUserMessageAdapter(Context context, ArrayList<ListMessengerActivity.ChatItem> chatItemArrayList) {
        this.context = context;
        this.chatItemArrayList = chatItemArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chatItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_user_messager, null);
            holder = new ViewHolder();
            holder.tvUsername = (TextView) convertView.findViewById(R.id.tv_name_user_send_mess);
            holder.tvLastMess = (TextView) convertView.findViewById(R.id.tv_mess_last);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel userModel = this.chatItemArrayList.get(position).getUserModel();
        holder.tvUsername.setText(userModel.getUsername());
        holder.tvLastMess.setText(chatItemArrayList.get(position).getLastTextMessage());


        return convertView;
    }

    static class ViewHolder {
        TextView tvUsername;
        TextView tvLastMess;
    }
}


