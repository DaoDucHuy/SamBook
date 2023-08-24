package com.sam.sambook.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sam.sambook.activity.EditProfileActivity;
import com.sam.sambook.R;
import com.sam.sambook.activity.ListMessengerActivity;
import com.sam.sambook.activity.LoginActivity;
import com.sam.sambook.adapter.BookAdapter;
import com.sam.sambook.fragmentProfileBook.PagerAdapter;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import customfonts.TextView_Lato_Regular;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton
//            floatingActionButton1,
            floatingActionButton2,
            floatingActionButton3;
    private TextView tvUserName, tvUserUserName, tvUserProfileAddress;
    private TextView_Lato_Regular tvNumbBook, tvNumbReview, tvNumbRequest;
    private ImageButton editProfile, imgbChat;

    private UserModel _userModel;
    private UserModel _userModelLocal;

    private static final int REQUEST_CODE = 0x9345;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        getUser();

        materialDesignFAM = (FloatingActionMenu) root.findViewById(R.id.material_design_android_floating_action_menu);
//        floatingActionButton1 = (FloatingActionButton) root.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) root.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) root.findViewById(R.id.material_design_floating_action_menu_item3);

        tvUserUserName = root.findViewById(R.id.tv_user_profile_username);
        tvUserName = root.findViewById(R.id.tv_user_profile_name);
        tvUserProfileAddress = root.findViewById(R.id.tv_user_profile_address);
        tvNumbBook = root.findViewById(R.id.tv_numb_book_user_profile);
        tvNumbReview = root.findViewById(R.id.tv_numb_review_user_profile);
        tvNumbRequest = root.findViewById(R.id.tv_numb_request_user_profile);
        editProfile = root.findViewById(R.id.fragment_profile_edit_profile);
        imgbChat = root.findViewById(R.id.imgb_chat);

        init();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "edit profile", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getApplicationContext(), EditProfileActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

//        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "Btn1", Toast.LENGTH_SHORT).show();
//            }
//        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "Btn2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), ListMessengerActivity.class);
                intent.putExtra("USERS", _userModel);
                startActivity(intent);

            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalDelete();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        imgbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListMessengerActivity.class);
                intent.putExtra("USERS", _userModel);
                startActivity(intent);
            }
        });


        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        final ViewPager viewPager = root.findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        final ViewPager viewPager = root.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("NAME");
                String address = data.getStringExtra("ADDRESS");
                String password = data.getStringExtra("PASSWORD");

                updateUserData(name, address, password);

//                Toast.makeText(getActivity().getApplicationContext(), name , Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), address , Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), password , Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    private void init() {

        if (_userModel.getName() != null) {
            tvUserName.setText(Html.fromHtml(_userModel.getName()));
        } else {
            tvUserName.setText("");
        }
        tvUserUserName.setText(_userModel.getUsername());
        tvUserProfileAddress.setText(Html.fromHtml(_userModel.getAddress()));

        if (_userModel.getBooksUser() != null) {
            tvNumbBook.setText(String.valueOf(_userModel.getBooksUser().size()));
        }
        if (_userModel.getBooksUserReview() != null) {
            tvNumbReview.setText(String.valueOf(_userModel.getBooksUserReview().size()));
        }
        if (_userModel.getBooksUserRequest() != null) {
            tvNumbRequest.setText(String.valueOf(_userModel.getBooksUserRequest().size()));
        }
    }

    private void getUser() {
        _userModel = (UserModel) getActivity().getIntent().getSerializableExtra("USER");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(_userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    _userModel = new UserModel();
                    _userModel = snapshot.getValue(UserModel.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("User", _userModel.getUsername());
    }

    private void updateUserData(String name, String address, String password) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        if (name.length() != 0) {
            _userModel.setName(name);
            tvUserName.setText(_userModel.getName());
            myRef.child("Users").child(_userModel.getUsername()).child("name").setValue(_userModel.getName());
        }

        if (address.length() != 0) {
            _userModel.setAddress(address);
            tvUserProfileAddress.setText(_userModel.getAddress());
            myRef.child("Users").child(_userModel.getUsername()).child("address").setValue(_userModel.getAddress());
        }

        if (password.length() != 0) {
            _userModel.setPassword(password);
            myRef.child("Users").child(_userModel.getUsername()).child("password").setValue(_userModel.getPassword());
        }
    }

    private void userLocalDelete() {
        try {
            String emptyString = "";
            OutputStream saveFile = getActivity().getApplicationContext().openFileOutput("USERSAMBOOK.txt", MODE_PRIVATE);
            saveFile.write(emptyString.getBytes());
            saveFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
