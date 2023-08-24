package com.sam.sambook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sam.sambook.R;
import com.sam.sambook.model.NotifyModel;

import java.util.ArrayList;

public class NotifyAdapter extends ArrayAdapter<NotifyModel> {

    private Context context;
    private int resourceLayout;

    public NotifyAdapter(@NonNull Context context, int resource, ArrayList<NotifyModel> notifyModelArrayList) {
        super(context, resource, notifyModelArrayList);
        this.resourceLayout = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view ==null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            ViewGroup root;
            view = vi.inflate(resourceLayout, null);
        }

        NotifyModel notifyModel = getItem(position);
        if (notifyModel != null) {
            TextView tvDate = view.findViewById(R.id.tv_day_notify);
            TextView tvTitle = view.findViewById(R.id.tv_title_notify);
            TextView tvDesNotify = view.findViewById(R.id.tv_des_notify);

            if (tvDate != null){
                tvDate.setText(notifyModel.getDate());
            }
            if (tvTitle != null) {
                tvTitle.setText(notifyModel.getTitle());
            }
            if (tvDesNotify != null) {
                tvDesNotify.setText(notifyModel.getDescription());
            }
        }

        return view;
    }
}
