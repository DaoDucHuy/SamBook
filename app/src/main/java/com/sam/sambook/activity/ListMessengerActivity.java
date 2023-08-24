package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.adapter.ListUserMessageAdapter;
import com.sam.sambook.model.ChatMessageModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListMessengerActivity extends AppCompatActivity {

    private UserModel _userModel;
    private ArrayList<ChatMessageModel> chatMessageModelList;
    private ListUserMessageAdapter listUserMessageAdapter;
    private ArrayList<UserModel> otherUserModelMessage;
    private ArrayList<ChatItem> chatItemArrayList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messeager);
        setTitle("Tin nhắn");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.lv_messager);
        otherUserModelMessage = new ArrayList<>();
        chatItemArrayList = new ArrayList<>();
        listUserMessageAdapter = new ListUserMessageAdapter(getApplicationContext(), chatItemArrayList);
        listView.setAdapter(listUserMessageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("OTHERUSER", chatItemArrayList.get(position).getUserModel());
                intent.putExtra("USER", _userModel);
                startActivity(intent);
            }
        });

        getUser();
        getMessage();
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUser();
        getMessage();
        reload();
    }

    private void getUser() {
        _userModel = (UserModel) getIntent().getSerializableExtra("USERS");
    }

    private void reload() {
        ArrayList<ChatItem> chatItemArrayListTemp = new ArrayList<>();

        for (int i = chatItemArrayList.size() - 1; i >= 0; i--) {
            boolean check = true;
            for (int j = 0; j < chatItemArrayListTemp.size(); j++) {
                if (Objects.equals(chatItemArrayListTemp.get(j).getUserModel().getUsername(), chatItemArrayList.get(i).getUserModel().getUsername())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                chatItemArrayListTemp.add(chatItemArrayList.get(i));
            }
        }

        chatItemArrayList = chatItemArrayListTemp;

        listUserMessageAdapter = new ListUserMessageAdapter(getApplicationContext(), chatItemArrayList);
        listView.setAdapter(listUserMessageAdapter);
    }


    //get all messages and filter with sender and receiver
    private void getMessage() {
        chatMessageModelList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chats");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    ChatMessageModel chatMessageModel = snapshot.getValue(ChatMessageModel.class);

                    if (chatMessageModel.getSender().getUsername().equals(_userModel.getUsername()) || chatMessageModel.getReceiver().getUsername().equals(_userModel.getUsername())) {
                        chatMessageModelList.add(chatMessageModel);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatMessageModelList.size() != 0) {
                    for (int i = 0; i < chatMessageModelList.size(); i++) {
                        if (Objects.equals(chatMessageModelList.get(i).getSender().getUsername(), _userModel.getUsername())) {
                            UserModel userModel = new UserModel(chatMessageModelList.get(i).getReceiver().getUsername());
                            otherUserModelMessage.add(userModel);
                            ChatItem chatItem = new ChatItem(userModel, chatMessageModelList.get(i).getMessageText());
                            chatItemArrayList.add(chatItem);
                        }
                        if (Objects.equals(chatMessageModelList.get(i).getReceiver().getUsername(), _userModel.getUsername())) {
                            UserModel userModel = new UserModel(chatMessageModelList.get(i).getSender().getUsername());
                            otherUserModelMessage.add(userModel);
                            ChatItem chatItem = new ChatItem(userModel, chatMessageModelList.get(i).getMessageText());
                            chatItemArrayList.add(chatItem);
                        }
                    }
                }
                reload();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ChatItem {
        UserModel userModel;
        String lastTextMessage;

        ChatItem(UserModel userModel, String lastTextMessage) {
            this.userModel = userModel;
            this.lastTextMessage = lastTextMessage;
        }

        public UserModel getUserModel() {
            return userModel;
        }

        public String getLastTextMessage() {
            return lastTextMessage;
        }
    }
}