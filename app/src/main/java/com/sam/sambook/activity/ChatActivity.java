package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.sambook.R;
import com.sam.sambook.adapter.MessageListAdapter;
import com.sam.sambook.model.ChatMessageModel;
import com.sam.sambook.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // Firebase instance variables
//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseUser mFirebaseUser;
//
//    private FirebaseListAdapter<ChatMessageModel> adapter;
//
    private UserModel _userModelSend, _userModelReceiver = new UserModel();
    private DatabaseReference databaseReference;
    private ChatMessageModel chatMessageModel;

    private RecyclerView recyclerView;
    private MessageListAdapter mMessageAdapter;
    private List<ChatMessageModel> chatMessageModelList = new ArrayList<>();

    private EditText edtTextMessage;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("");

        //Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        // Initialize Firebase Auth
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();

//        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
//        mMessageAdapter = new MessageListAdapter(this, messageList);
//        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        even();

        readMessages();
    }

    //Setup back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void even() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtTextMessage.getText().toString();
                if (!msg.equals("")){
                    chatMessageModel = new ChatMessageModel(msg, _userModelSend, _userModelReceiver);
                    sendMessage(chatMessageModel);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn không thể nhắn tin nhắn trống", Toast.LENGTH_SHORT).show();
                }
                edtTextMessage.setText("");
            }
        });
    }

    private void init() {
        edtTextMessage = findViewById(R.id.edittext_chatbox);
        btnSend = findViewById(R.id.button_chatbox_send);
        recyclerView = findViewById(R.id.reyclerview_message_list);

        recyclerView.setHasFixedSize(true);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        _userModelSend = (UserModel) getIntent().getSerializableExtra("USER");
        _userModelSend = new UserModel(_userModelSend.getUsername());

        _userModelReceiver = (UserModel) getIntent().getSerializableExtra("OTHERUSER");
        _userModelReceiver = new UserModel(_userModelReceiver.getUsername());
    }

    private void sendMessage(ChatMessageModel chatMessageModel){
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", chatMessageModel.getSender().getUsername());
//        hashMap.put("receiver",chatMessageModel.getReceiver().getUsername());
//        hashMap.put("message", chatMessageModel.getMessageText());

        databaseReference.push().setValue(chatMessageModel);
    }

    private void readMessages(){
        chatMessageModelList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessageModelList.clear();
//                ChatMessageModel chatMessageModel1 = snapshot.getValue(ChatMessageModel.class);
//                chatMessageModelList.add(chatMessageModel1);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    ChatMessageModel chatMessageModel = dataSnapshot.getValue(ChatMessageModel.class);
//                    chatMessageModelList.add(chatMessageModel);
                    ChatMessageModel chatMessageModel = dataSnapshot.getValue(ChatMessageModel.class);
                    if (chatMessageModel.getSender().getUsername().equals(_userModelSend.getUsername()) && chatMessageModel.getReceiver().getUsername().equals(_userModelReceiver.getUsername())) {
//                        userModelList.add(chatMessageModel.getSender());
                        chatMessageModelList.add(chatMessageModel);
                    }
                    if (chatMessageModel.getSender().getUsername().equals(_userModelReceiver.getUsername()) && chatMessageModel.getReceiver().getUsername().equals(_userModelSend.getUsername())){
                        chatMessageModelList.add(chatMessageModel);
                    }

                }

                mMessageAdapter = new MessageListAdapter(getApplicationContext(),chatMessageModelList, _userModelSend);
                recyclerView.setAdapter(mMessageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}