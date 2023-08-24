package com.sam.sambook.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.adapter.NotifyAdapter;
import com.sam.sambook.model.NotifyModel;
import com.sam.sambook.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NotificationFragment extends Fragment {

    private NotifyModel _notifyModel;
    private ArrayList<NotifyModel> notifyModelArrayList;
    private UserModel _userModel;
    private NotifyAdapter notifyAdapter;

    private ListView listView;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notification, container, false);

        getNotify();

        listView = root.findViewById(R.id.list_notify);
        notifyModelArrayList = new ArrayList<>();
        notifyAdapter = new NotifyAdapter(getActivity().getApplicationContext(), R.layout.item_notify, notifyModelArrayList);
        listView.setAdapter(notifyAdapter);

        return root;
    }

    private void getNotify() {
        _userModel = (UserModel) getActivity().getIntent().getSerializableExtra("USER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _userModel = new UserModel();
                    _userModel = snapshot.getValue(UserModel.class);

                    if (_userModel.getNotifyModelList() != null) {
                        notifyModelArrayList = (ArrayList<NotifyModel>) _userModel.getNotifyModelList();
                        notifyAdapter = new NotifyAdapter(getActivity().getApplicationContext(), R.layout.item_notify, notifyModelArrayList);
                        listView.setAdapter(notifyAdapter);
                    } else {
                        notifyModelArrayList = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}